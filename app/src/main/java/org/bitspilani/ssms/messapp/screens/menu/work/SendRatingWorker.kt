package org.bitspilani.ssms.messapp.screens.menu.work

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import org.bitspilani.ssms.messapp.MessApp
import org.bitspilani.ssms.messapp.di.screens.menu.MenuScreenModule
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.data.retrofit.MenuService
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenuItemsDao
import org.bitspilani.ssms.messapp.screens.menu.data.room.model.DataLayerMenuItem
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.util.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class SendRatingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    @Inject
    lateinit var menuItemsDao: MenuItemsDao

    @Inject
    lateinit var menuService: MenuService

    @Inject
    lateinit var userRepository: UserRepository


    @SuppressLint("CheckResult")
    override fun doWork(): Result {
        MessApp.appComponent.newMenuScreenComponent(MenuScreenModule()).inject(this)

        menuItemsDao.getAllRatedMenuItems()
            .single(listOf())
            .filter { _items -> _items.isNotEmpty() }
            .map { _items ->
                JSONObject().apply {
                    put("upvote"  , _items.filter { it.rating == Rating.Positive }.toJSONArray())
                    put("downvote", _items.filter { it.rating == Rating.Negative }.toJSONArray())
                }.toRequestBody()
            }
            .zipWith(userRepository.getUser().map { it.jwt }, BiFunction { t1: RequestBody, t2: String -> Pair(t1, t2) })
            .flatMap {
                menuService.rateItems(it.second, it.first).toMaybe<Unit>().subscribeOn(Schedulers.io())
            }
            .subscribeOn(Schedulers.io())
            .blockingGet(Unit)

        return Result.success()
    }

    private fun List<DataLayerMenuItem>.toJSONArray() {
        val jsArray = JSONArray()
        groupBy { it.mealId }.map {
            JSONObject().apply {
                put("menu_id", it.key)
                val itemIds = it.value.map { it.itemId }.toJSONArray()
                put("item_ids", itemIds)
            }
        }.forEach {
            jsArray.put(it)
        }
        return
    }

    @JvmName(name = "ListOfLongToJSONArray")
    private fun List<Long>.toJSONArray(): JSONArray {
        return JSONArray(this)
    }
}