package QishloqHojalik.Paxtachi.Enums

enum class Specialization(
    val nameUz: String,
    val icon: String
) {

    PAHTA_GALLA("Paxta–G'alla", "wheat"),
    ORCHARD("Bog'", "trees"),
    GRAPE("Uzumchilik", "grape"),
    VEGETABLE("Sabzavotchilik", "carrot"),
    GREENHOUSE("Issiqxona", "warehouse"),
    LIVESTOCK("Chorvachilik", "beef"),
    POULTRY("Parrandachilik", "bird"),
    FISH("Baliqchilik", "fish"),
    BEEKEEPING("Asalarichilik", "bug"),
    TREE_NURSERY("Ko'chatchilik", "sprout"),
    OTHER("Boshqa", "help-circle")

}