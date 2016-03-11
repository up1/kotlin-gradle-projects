package griffio.shop

import org.junit.Test
import kotlin.test.assertEquals

class ProductTest {

    val threeForOne = object : Offer {
        override fun value(): Double {
            return 3.div(1).toDouble()
        }
    }

    val tenPercent = object : Offer {
        override fun value(): Double {
            return 10.0
        }
    }

    val sumoTangerines = Product("Sumo Tangerines", 5.99, ProductSku("SKU-0947"))

    val plasticSpoons = Product("Plastic Spoons", 7.99, ProductSku("SKU-1002"))

    val aloeVera = Product("Aloe Vera", 1.99, ProductSku("SKU-1112"))

    val lobsterMacCheese = Product("Lobster Mac & Cheese", 5.50, ProductSku("SKU-331"))

    @Test
    fun three_for_one_savings() {

        val basket = listOf(sumoTangerines, sumoTangerines, sumoTangerines, plasticSpoons, aloeVera, aloeVera, aloeVera).groupBy { it }

        // two ways to create total savings

        val savingsBySum = basket.map { Discount(it.value.size, it.key.price, threeForOne).savings() }.sumByDouble { it }

        val savingsByReduce = basket.map { Discount(it.value.size, it.key.price, threeForOne).savings() }.reduce { total, saving -> total.plus(saving) }

        assertEquals(String.format("%.2f.%n", aloeVera.price.plus(sumoTangerines.price)), String.format("%.2f.%n", savingsBySum))

        assertEquals(String.format("%.2f.%n", aloeVera.price.plus(sumoTangerines.price)), String.format("%.2f.%n", savingsByReduce))
    }

    @Test
    fun ten_percent_off_savings() {

        val basket = listOf(aloeVera, aloeVera, aloeVera)

        val savingsBySum = DiscountTest.PercentDiscount(basket.size, aloeVera.price, tenPercent).savings()

        assertEquals(String.format("%.2f.%n", aloeVera.price.times(basket.size) * 0.10), String.format("%.2f.%n", savingsBySum))
    }

    @Test
    fun ten_percent_off_savings_if_total_amount_over_15() {

        val basket = listOf(lobsterMacCheese, lobsterMacCheese, lobsterMacCheese)

        val total = basket.sumByDouble { it.price }

        val savingsBySum = DiscountTest.ConditionalDiscount({ total > 15.0 }, basket.size, total / basket.size, tenPercent).savings()

        assertEquals(String.format("%.2f.%n", total * 0.10), String.format("%.2f.%n", savingsBySum))
    }
}