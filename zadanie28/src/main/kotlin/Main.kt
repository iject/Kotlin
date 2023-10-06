import kotlin.random.Random
import kotlin.math.*
interface Transport{
    var speed: Double
    var price: Double
    var accidentRate: Double
    fun totalTime(c1: City, c2: City):Double{
        return c1.dist(c2)/speed
    }
    fun totalCost(c1: City, c2: City):Double {
        return c1.dist(c2) * price    }
}
class Car():Transport{
    override var speed = 60.0
    override var price = 200.0
    override var accidentRate = 0.01
}
class Train():Transport{
    override var speed = 50.0
    override var price = 170.0
    override var accidentRate = 0.001}
class Avia():Transport{
    override var speed = 600.0
    override var price = 1000.0
    override var accidentRate = 0.0001
}
class City(var size: Int, var x: Int, var y:Int){
    var weather: Boolean = true or false

    fun dist(c: City):Double{
        return sqrt(((x-c.x)*(x-c.x)+(y-c.y)*(y-c.y)).toDouble())    }
}

class Client(var c1: City, var c2: City, var weight: Double = 0.0){
    var time: Int = 0
    var cost: Double = 0.0
    var priority = 1
    constructor(c1: City, c2: City, weight: Double, _time: Int):this(c1,c2, weight){
        time = _time
        priority = 1    }
    constructor(c1: City, c2: City, weight: Double, _cost:Double):this(c1,c2, weight){
        cost = _cost
        priority = 0
    }
    constructor(c1: City, c2: City, weight: Double,
                _time: Int, _cost:Double):this(c1,c2, weight, _time){
        cost = _cost
    }
    constructor(c1: City, c2: City, weight: Double,
                _cost:Double, _time: Int):this(c1,c2, weight, _cost){
        time = _time
    }
    fun order():Array<Any> {
        var totalTime : Double = 0.0
        var totalCost : Double = 0.0
        var car = Car()
        var train = Train()
        var avia = Avia()
        var transport: Transport = car
        if(c1.size==0 || c2.size==0){
            totalTime = car.totalTime(c1,c2)
            totalCost = car.totalCost(c1,c2)
            transport = car

        }
        else{
            if(c1.size==1 || c2.size==1){
                if(priority==1){
                    if(train.totalTime(c1,c2) < time){
                        totalTime = train.totalTime(c1,c2)
                        totalCost = train.totalCost(c1,c2)
                        transport = train
                    }
                    else{
                        totalTime = car.totalTime(c1,c2)
                        totalCost = car.totalCost(c1,c2)
                        transport = car
                    }
                }
                else
                {
                    if(car.totalCost(c1, c2) < cost)
                    {
                        totalTime = car.totalTime(c1,c2)
                        totalCost = car.totalCost(c1,c2)
                        transport = car
                    }
                    else
                    {
                        totalTime = train.totalTime(c1,c2)
                        totalCost = train.totalCost(c1,c2)
                        transport = train
                    }
                }
            }else{
                if (priority == 1){
                    if (train.totalTime(c1,c2) < time){
                        totalTime = train.totalTime(c1,c2)
                        totalCost = train.totalCost(c1,c2)
                        transport = train
                    } else if (car.totalTime(c1,c2) < time){
                        totalTime = car.totalTime(c1,c2)
                        totalCost = car.totalCost(c1,c2)
                        transport = car
                    } else if (c1.weather == true && c2.weather == true){
                        totalTime = avia.totalTime(c1,c2)
                        totalCost = avia.totalCost(c1,c2)
                        transport = avia
                    }else
                    {
                        totalTime = car.totalTime(c1,c2)
                        totalCost = car.totalCost(c1,c2)
                        transport = car
                    }                    }
                else
                {
                    if(avia.totalCost(c1, c2) < cost){
                        if(c1.weather == true && c2.weather == true){

                        totalTime = avia.totalTime(c1,c2)
                        totalCost = avia.totalCost(c1,c2)
                            transport =avia
                    }
                    else if(car.totalCost(c1, c2) < cost)
                    {
                        totalTime = car.totalTime(c1,c2)
                        totalCost = car.totalCost(c1,c2)
                        transport = car
                    }
                    else
                    {
                        totalTime = train.totalTime(c1,c2)
                        totalCost = train.totalCost(c1,c2)
                        transport = train
                    }
                }
            }
        }

        }
        var r= Random.nextDouble(0.0,0.99)
        if (r < transport.accidentRate){
            TRANSagent.balance -=2*totalCost
        }else {
        TRANSagent.balance = TRANSagent.balance + totalCost*weight
        TRANSagent.time+=totalTime
        TRANSagent.count+=1}
    return arrayOf(totalCost, totalTime)
    }
}
class TRANSagent()
{
    companion object {
        var balance = 0.0
        var time = 0.0
        var count = 0
        fun showInfo() {
            println("Balance: ${balance}, average time = ${time / count}")
        }
    }
}
fun main(){
    var Moscow = City(2, 0, 0)
    var Ulyanovsk = City(1, 345, 432)
    var Ufa = City(2, 123, 300)
    var Ekaterinburg = City(2, 456, 701)
    var Kirov = City(1, 666, 221)
    var Asbest = City(0,888, 999)
    var cities = arrayOf(Moscow, Ulyanovsk, Ufa,
        Ekaterinburg, Kirov, Asbest)


    var clients= arrayOf(Client(Kirov, Asbest, 100.0),
    Client (Ufa, Ulyanovsk, 10000.1, 10000000),
    Client (Moscow, Ekaterinburg, 1.0, 25000000.0))
    for (cl in clients){
       var clinf= cl.order()
        println(clinf[0])
        println(clinf[1])
    }

    TRANSagent.showInfo()

}