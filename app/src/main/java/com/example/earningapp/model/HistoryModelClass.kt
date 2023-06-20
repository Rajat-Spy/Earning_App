package com.example.earningapp.model

class HistoryModelClass {
    var timeAndDate: String = ""
    var coin: String = ""
    var isWithdrawl: Boolean = false

    constructor()
    constructor(timeAndDate: String, coin: String, isWithdrawl: Boolean) {
        this.timeAndDate = timeAndDate
        this.coin = coin
        this.isWithdrawl = isWithdrawl
    }

}
