package hu.kole.nestedlist.repo

import hu.kole.nestedlist.data.Item
import hu.kole.nestedlist.data.Section
import kotlin.random.Random

class SectionRepository {

    fun getSections(): List<Section> {
        val list = mutableListOf<Section>()

        for (i in 1 until 8) {
            list.add(Section("Section$i", getItems()))
        }

        return list
    }

    private fun getItems(): List<Item> {
        val list = mutableListOf<Item>()

        val size = Random.nextInt(4, 10)

        for (i in 0 until size) {
            list.add(Item("Item-$i"))
        }

        return list
    }
}