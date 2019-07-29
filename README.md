# KoleNestedList

Simple library for handle nested horizontal list in a vertical list.
You can select day by swiping, tapping on selector. You can customize all essential view on picker. Basically the picker use a default implementation for display dates.

![How does it look like](https://github.com/kgeriiie/KoleNestedList/blob/master/promo-res/promo.gif)

# Usage

##### Put into the layout resource file
Include the day cells container in your layout. You can customize (size, color, font) the title of container.

You can use it in a regular way. Just create two Adapter, one for sections and one for section items.

For section's adpter:

```java
        class SectionAdapter: BaseNestedListAdapter<Section, Item, BaseNestedListAdapter.BaseNestedViewHolder<Item, Section>>(SectionDiffCallback()) {
            override fun getNestedItems(position: Int): List<Item> {
                return getItem(position).data
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseNestedViewHolder<Item, Section> {
                return SectionViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_section, parent, false))
            }

            override fun onBindViewHolder(holder: BaseNestedViewHolder<Item, Section>, position: Int) {
                holder.setup(position, getItem(position))
            }
        }
```
For the item's adapter you can use your own custom adapter.

In your *BaseNestedViewHolder* you have to setup the nested view holder's adapter and that's it.

```java
    override fun setup(position: Int, section: Section?) {
        section?.let {
            binding.recyclerView.adapter = adapter

            this.submitList(section.data)
        }
    }
```

# Download
### Gradle
```groovy
implementation 'hu.kole.nestedlist:nestedlist:0.1'
```