package com.example.forthapp_2

import android.icu.text.CaseMap.Title
import android.util.EventLogTags.Description

class User {
    var Title:String?=null
    var Descript:String?=null
    var id:String?=null
    constructor(){}
    constructor(Title: String?, Descript: String?, id: String?) {
        this.Title = Title
        this.Descript = Descript
        this.id = id
    }
}