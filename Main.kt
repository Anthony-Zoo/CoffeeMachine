package machine


enum class States {
    ACTION, BUY, WATER_INPUT, MILK_INPUT, BEANS_INPUT, CUPS_INPUT, OFF
}

enum class Actions(val desc: String) {
    BUY("buy"),
    FILL("fill"),
    TAKE("take"),
    REMAINING("remaining"),
    EXIT("exit")
}

enum class Coffees(val water: Int,
                   val milk: Int,
                   val beans: Int,
                   val money: Int) {

    ESPRESSO(250, 0, 16, 4 ),
    LATTE(350, 75, 20, 7),
    CAPPUCCINO(200, 100, 12, 6)
}

enum class Prompts(val message: String) {
    ACTION("Write action (buy, fill, take, remaining, exit):"),
    BUY("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:"),
    WATER("Write how many ml of water you want to add:"),
    MILK("Write how many ml of milk you want to add:"),
    BEANS("Write how many grams of coffee beans you want to add:"),
    CUPS("Write how many disposable cups you want to add:")
}

class CoffeeMachine(var water: Int,
                    var milk: Int,
                    var beans: Int,
                    var cups: Int,
                    var money: Int) {

    var machineState: States = States.ACTION
    var input: String = ""

    fun processInput(input: String) {
        this.input = input

        when (this.machineState) {
            States.ACTION -> processAction()
            States.WATER_INPUT -> fill()
            States.MILK_INPUT -> fill()
            States.BEANS_INPUT -> fill()
            States.CUPS_INPUT -> fill()
            States.BUY -> buy()
        }
    }

    fun processAction() {
        when (this.input) {
            Actions.REMAINING.desc -> status()
            Actions.TAKE.desc -> take()
            Actions.BUY.desc -> buy()
            Actions.FILL.desc -> fill()
            Actions.EXIT.desc -> this.machineState = States.OFF
        }
    }

    fun status() {
        println("The coffee machine has:")
        println("${this.water} ml of water")
        println("${this.milk} of milk")
        println("${this.beans} g of beans")
        println("${this.cups} disposable cups")
        println("$${this.money} of money")
    }

    fun buy() {
        when (this.machineState) {
            States.ACTION -> {
                println(Prompts.BUY.message)
                this.machineState = States.BUY
            }
            States.BUY -> {
                when {
                    this.input == "back" -> this.machineState = States.ACTION
                    this.input.toInt() == 1 -> make(Coffees.ESPRESSO)
                    this.input.toInt() == 2 -> make(Coffees.LATTE)
                    this.input.toInt() == 3 -> make(Coffees.CAPPUCCINO)
                }
            }
        }
    }

    fun make(coffee: Coffees) {
        val enough: Boolean = checkResources(coffee.water, coffee.milk, coffee.beans)

        if (enough) {
            this.water -= coffee.water
            this.beans -= coffee.beans
            this.milk -= coffee.milk
            this.cups -= 1
            this.money += coffee.money
        }
        this.machineState = States.ACTION
    }

    fun checkResources(reqWater: Int, reqMilk: Int, reqBeans: Int): Boolean {
        when {
            this.water < reqWater -> {
                println("Sorry, not enough water!")
                return false
            }
            this.milk < reqMilk -> {
                println("Sorry, not enough milk!")
                return false
            }
            this.beans < reqBeans -> {
                println("Sorry, not enough coffee beans!")
                return false
            }
            this.cups < 1 -> {
                println("Sorry, not enough cups!")
                return false
            }
            else -> {
                println("I have enough resources, making you a coffee!")
                return true
            }
        }
    }

    fun fill() {
        when (this.machineState) {
            States.ACTION -> {
                println(Prompts.WATER.message)
                this.machineState = States.WATER_INPUT
            }
            States.WATER_INPUT -> {
                this.water += this.input.toInt()
                println(Prompts.MILK.message)
                this.machineState = States.MILK_INPUT
            }
            States.MILK_INPUT -> {
                this.milk += this.input.toInt()
                println(Prompts.BEANS.message)
                this.machineState = States.BEANS_INPUT
            }
            States.BEANS_INPUT -> {
                this.beans += this.input.toInt()
                println(Prompts.CUPS.message)
                this.machineState = States.CUPS_INPUT
            }
            States.CUPS_INPUT -> {
                this.cups += this.input.toInt()
                this.machineState = States.ACTION
            }
        }
    }

    fun take() {
        println("I gave you $${this.money}")
        this.money = 0
    }

}

fun main() {
    val coffeeMachine: CoffeeMachine = CoffeeMachine(water = 400, milk = 540, beans = 120, cups = 9, money = 550)

    while (coffeeMachine.machineState != States.OFF) {
        if (coffeeMachine.machineState == States.ACTION) {
            println(Prompts.ACTION.message)
        }

        coffeeMachine.processInput(readln())
    }
}