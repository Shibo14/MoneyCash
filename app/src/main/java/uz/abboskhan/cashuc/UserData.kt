package uz.abboskhan.cashuc

class UserData {

    var id: String = ""
    var timestamp :Long =0
    var name:String=""
    var email: String = ""
    var password: String = ""
    var referCode: String = ""
    var coins: Long? = 0
    var diamond: Long? = 0


    constructor(
        id: String,
        timestamp: Long,
        name: String,
        email: String,
        password: String,
        referCode: String,
        coins: Long?,
        diamond: Long?
    ) {
        this.id = id
        this.timestamp = timestamp
        this.name = name
        this.email = email
        this.password = password
        this.referCode = referCode
        this.coins = coins
        this.diamond = diamond
    }

    constructor()
}