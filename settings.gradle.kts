plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "PizzaTimeService"

include("PizzaTime.Communication", "PizzaTime.OrderService","PizzaTime.Gateway", "PizzaTime.pizzaidph2")
