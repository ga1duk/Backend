package ru.netology.nmedia.database.typeconverter

import androidx.room.TypeConverter
import ru.netology.nmedia.dto.AttachmentType

class AttachmentConverter {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)

    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
}