package ru.netology.nmedia.database.typeconverter

import androidx.room.TypeConverter
import ru.netology.nmedia.dto.Attachment

class AttachmentConverter {

    @TypeConverter
    fun attachmentToString(attachment: Attachment?): String? {
        return attachment?.url
    }

    @TypeConverter
    fun stringToAttachment(url: String?): Attachment? {
        return if (url != null) Attachment(url = url) else null
    }
}