package org.bitspilani.ssms.messapp.screens.grub.shared.data.room

import androidx.room.TypeConverter
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodOption
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodType
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.SigningStatus
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Slot

class GrubBatchesTypeConverters {

    @TypeConverter
    fun fromFoodOptionToString(fo: FoodOption): String = fo.toString()

    @TypeConverter
    fun fromStringToFoodOption(st: String): FoodOption = FoodOption.valueOf(st)

    @TypeConverter
    fun fromFoodTypeToString(ft: FoodType): String = ft.toString()

    @TypeConverter
    fun fromStringToFoodType(st: String): FoodType = FoodType.valueOf(st)

    @TypeConverter
    fun fromSigningStatusToString(ss: SigningStatus): String = ss.toString()

    @TypeConverter
    fun fromStringToSigningStatus(st: String): SigningStatus = SigningStatus.valueOf(st)

    @TypeConverter
    fun fromSlotToString(sl: Slot?): String = sl.toString()

    @TypeConverter
    fun fromStringToSlot(st: String): Slot? = if(st != "null") { Slot.valueOf(st) } else { null }
}