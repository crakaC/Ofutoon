package com.crakac.ofutoon.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crakac.ofutoon.api.entity.Account
import com.google.gson.Gson

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var domain: String = "",
    var token: String = "",
    var accountJson: String = ""
) {
    @Transient
    private var _account: Account? = null
    var account: Account
        get() {
            if (_account == null) {
                _account = Gson().fromJson(accountJson, Account::class.java)
            }
            return _account!!
        }
        set(value) {
            _account = value
        }
    val nameWithDomain get() = "$name@$domain"
}