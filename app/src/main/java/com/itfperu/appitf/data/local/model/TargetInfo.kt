package com.itfperu.appitf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class TargetInfo {
    @PrimaryKey(autoGenerate = true)
    var targetId: Int = 0
    var usuario: String = ""
    var nroContacto: Int = 0
    var targetDetId: Int = 0
}