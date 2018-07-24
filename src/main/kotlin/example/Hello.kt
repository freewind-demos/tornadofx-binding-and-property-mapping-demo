package example

import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyProperty
import javafx.beans.value.ObservableValue
import tornadofx.*

class HelloWorld : View() {
    override val root = vbox {
        val input1 = textfield()
        label(input1.textProperty().map { "$it!!" })
        val input2 = textfield()
        label(listOf(input1.textProperty(), input2.textProperty()).bindingMap { (t1, t2) -> "$t1 & $t2" })
    }
}

fun <T : Any, K> List<ObservableValue<T>>.bindingMap(fn: (List<T>) -> K): ObservableValue<K> {
    return Bindings.createObjectBinding({ fn(this.map { it.value }) }, this.toTypedArray())
}

fun <T, K> ReadOnlyProperty<T>.map(fn: (T) -> K): ReadOnlyProperty<K> {
    val source = this
    return ReadOnlyObjectWrapper<K>().apply {
        source.addListener { _, _, newValue -> this.value = fn(newValue) }
    }
}

class HelloWorldStyle : Stylesheet() {
    init {
        root {
            prefWidth = 400.px
            prefHeight = 400.px
        }
    }
}

class HelloWorldApp : App(HelloWorld::class, HelloWorldStyle::class)

fun main(args: Array<String>) {
    launch<HelloWorldApp>()
}