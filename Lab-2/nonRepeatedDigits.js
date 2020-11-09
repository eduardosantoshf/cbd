nonRepeatedDigits = function() {
    var fullNumber = db.phones.find({},{"display": 1, "_id": 0}).toArray();

    var nonRepeatedDigits = []
    for (var i = 0; i < fullNumber.length; i++){
        var n = fullNumber[i].display
        n = n.split("-")[1]
        
        var a = []
        var nonRepeating = true

        for( var j = 0; j < n.length; j++){
            if (a.includes(n[j])){
                nonRepeating = false
                break
            }
            a.push(n[j])
        }

        if (nonRepeating){
            nonRepeatedDigits.push(fullNumber[i])
        }
    }

    return nonRepeatedDigits
}