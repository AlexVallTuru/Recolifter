package cat.copernic.pdiaza.recolifter.databaseManager

import cat.copernic.pdiaza.recolifter.models.DataRecycleProduct
import cat.copernic.pdiaza.recolifter.models.DataTree
/**
 *Classe para hacer Sets manuales a Firebase
 */
class FirebaseSets {
    private lateinit var firebaseCompadre: FirebaseReadWrite

    fun addFirebaseCat(){
        firebaseCompadre = FirebaseReadWrite.initialize()

        val arbol1 = DataTree(
            "-150kg",
            590,
            "El Baobab és una especie nativa de Madagascar i difundida a Àfrica. Famoso per la seva grandeza, el diàmetre del tronc pot alcanzar els 10 metres. Es considera com el centre de gravetat de la vida social dels pobles: sota de la seva sombra se llevan. a cabo mercados, reuniones, bailes y juegos. Se planta per contribuir a la recuperació dels boscos naturals.",
            "https://cdn.pixabay.com/photo/2016/07/28/08/50/sunbeams-1547273_960_720.jpg",
            "Italia",
            "Baobab")

        firebaseCompadre.setTreeLanguage(arbol1, "CAT", "Baobab")

        val arbol2 = DataTree(
            "-250kg",
            300,
            "Natiu de l'Índia i Birmània, el Neem és un arbre de fulla perenne que pot arribar fins als 30 metres d'alçada. S'utilitza com a arbre per oferir ombra, gràcies al seu espès fullatge, i com a barrera contra la desertificació, especialment a les regions del Sàhara, però la seva peculiaritat és, sobretot, el que indica el seu nom en sànscrit: Sarva roda nidarini, el sanador de tots els mals, n'hi ha una quantitat, gairebé infinita, d'usos per a cada part d'aquest arbre, des de l'escorça fins a les fulles.",
            "https://cdn.pixabay.com/photo/2018/02/13/23/41/nature-3151869_960_720.jpg",
            "Tanzania",
            "Neem")

        firebaseCompadre.setTreeLanguage(arbol2, "CAT", "Neem")

        val arbol3 = DataTree(
            "-350kg",
            482,
            "Las palmeras cocoteras no solo son un elemento que caracteriza el paisaje de numerosas playas tropicales (de hecho, tienen predilección por los suelos arenosos), sino que también son árboles que dan frutos, las nueces de coco, cuyos usos son múltiples. El más conocido es obviamente el alimentario, que abarca tanto la pulpa fresca del fruto como su «leche». Pero de las nueces de coco se puede obtener también un aceite vegetal, con la cáscara de las nueces de coco se puede producir coir (una fibra textil empleada para fabricar cuerdas y alfombras), mientras que con la linfa se elabora el conocido como «vino de palma».",
            "https://universopalmeras.com/wp-content/uploads/2020/04/cocoteros-scaled.jpg",
            "Italia",
            "Palmera Cocotera")

        firebaseCompadre.setTreeLanguage(arbol3, "CAT", "Palmera Cocotera")

        val arbol4 = DataTree(
            "-350kg",
            482,
            "El roure espinós és un arbust de fulla perenne i sol assolir uns 3 metres d'alçada. Amb el temps, algunes de les flors formen els fruits característics del roure espinós. La seva varietat és estén per gairebé tota la regió mediterrània. Es cultiva principalment pels seus fruits, que es poden menjar rostits, per exemple.",
            "https://p4.wallpaperbetter.com/wallpaper/585/275/692/forest-mist-fog-misty-wallpaper-preview.jpg",
            "Italia",
            "Roure espinós")

        firebaseCompadre.setTreeLanguage(arbol4, "CAT", "Roure espinós")

        val product1 = DataRecycleProduct(
            "8412771104856",
            "Llet",
            50,
            "https://firebasestorage.googleapis.com/v0/b/recolifter.appspot.com/o/recycleProducts%2Fleche.png?alt=media&token=0a7099a3-d373-40b6-bf91-97ed760a9e07",
            null
        )
        firebaseCompadre.setProductsScanLanguage(product1, "CAT", product1.scanCode)
    }

    fun addFirebaseEs(){
        firebaseCompadre = FirebaseReadWrite.initialize()

        val arbol1 = DataTree(
            "-150kg",
            590,
            "El Baobab es una especie nativa de Madagascar y difundida a África. Famoso por su grandeza, el diámetro del tronco puede alcanzar los 10 metros. Se considera como el centro de gravedad de la vida social de los pueblos: debajo de su sombra se llevan. a cabo mercados, reuniones, bailes y juegos. Se planta para contribuir a la recuperación de los bosques naturales.",
            "https://cdn.pixabay.com/photo/2016/07/28/08/50/sunbeams-1547273_960_720.jpg",
            "Italia",
            "Baobab")

        firebaseCompadre.setTreeLanguage(arbol1, "ES", "Baobab")

        val arbol2 = DataTree(
            "-250kg",
            300,
            "Nativo de India y Birmania, el Neem es un árbol de hoja perenne que puede llegar hasta los 30 metros de altura. Se utiliza como árbol para ofrecer sombra, gracias a su espeso follaje, y como barrera contra la desertificación, especialmente en las regiones del Sáhara, pero su peculiaridad es, sobre todo, el que indica su nombre en sánscrito: Sarva rueda nidarini, el sanador de todos los males, hay una cantidad, casi infinita, de usos para cada parte de este árbol, desde la corteza hasta las hojas.",
            "https://cdn.pixabay.com/photo/2018/02/13/23/41/nature-3151869_960_720.jpg",
            "Tanzania",
            "Neem")

        firebaseCompadre.setTreeLanguage(arbol2, "ES", "Neem")

        val arbol3 = DataTree(
            "-350kg",
            482,
            "Las palmeras cocoteras no solo su un elemento que caracteriza lo paisaje de numerosas playas tropicales (de hecho, tienen predilección miedo los suelos arenosos), sino que también su árboles que dan frutos, las nueces de coco, cuyos usos su múltiplos. El más conocido se obviamente el alimentario, que abarca tanto la pulpa fresca del fruto como su «leche». Pero de las nueces de coco se puede obtener también un aceite vegetal, cono la cáscara de las nueces de coco se puede producir coir (una fibra textil empleada para fabricar cuerdas y alfombras), mientras que cono la linfa se elabora el conocido como «vino de palma».",
            "https://universopalmeras.com/wp-content/uploads/2020/04/cocoteros-scaled.jpg",
            "Italia",
            "Palmera Cocotera")

        firebaseCompadre.setTreeLanguage(arbol3, "ES", "Palmera Cocotera")

        val arbol4 = DataTree(
            "-350kg",
            482,
            "\n" +
                    "El roble espinoso es un arbusto de hoja perenne y suele lograr unos 3 metros de altura. Con el tiempo, algunas de las flores forman los frutos característicos del roble espinoso. Su variedad es extiende por casi toda la región mediterránea. Se cultiva principalmente por sus frutos, que se pueden comer asados, por ejemplo.",
            "https://p4.wallpaperbetter.com/wallpaper/585/275/692/forest-mist-fog-misty-wallpaper-preview.jpg",
            "Italia",
            "Roble Espinoso")

        firebaseCompadre.setTreeLanguage(arbol4, "ES", "Roure espinós")

        val product1 = DataRecycleProduct(
            "8412771104856",
            "Leche",
            50,
            "https://firebasestorage.googleapis.com/v0/b/recolifter.appspot.com/o/recycleProducts%2Fleche.png?alt=media&token=0a7099a3-d373-40b6-bf91-97ed760a9e07",
            null
        )
        firebaseCompadre.setProductsScanLanguage(product1, "ES", product1.scanCode)
    }


    fun addFirebaseEn(){
        firebaseCompadre = FirebaseReadWrite.initialize()

        val arbol1 = DataTree(
            "-150kg",
            590,
            "The Baobab is a species native to Madagascar and found in Africa. Famous for its grandeza, the trunk diameter can reach 10 metres. It is considered to be the centre of gravity of the social life of peoples: beneath its shadow it is removed. cabo mercados, reuniones, bailes y juegos. It is planted to contribute to the recovery of natural forests.",
            "https://cdn.pixabay.com/photo/2016/07/28/08/50/sunbeams-1547273_960_720.jpg",
            "Italy",
            "Baobab")

        firebaseCompadre.setTreeLanguage(arbol1, "EN", "Baobab")

        val arbol2 = DataTree(
            "-250kg",
            300,
            "Native to India and Burma, the Neem is a evergreen tree that can reach as high as 30 meters. It is used as a tree to offer shade, thanks to its thick foliage, and as a barrier to desertification, especially in the Sahara regions, but its peculiarity is, above all, what indicates its Sanskrit name: Sarva rolls nidarini, the healer of all evils, there is an almost infinite amount of use for each part of this tree, from bark to leaves.",
            "https://cdn.pixabay.com/photo/2018/02/13/23/41/nature-3151869_960_720.jpg",
            "Tanzanian",
            "Neem")

        firebaseCompadre.setTreeLanguage(arbol2, "EN", "Neem")

        val arbol3 = DataTree(
            "-350kg",
            482,
            "The palmeras cocoteras are not only an element that characterizes the paisaje de numerosas playas tropicales (de hecho, tienen predilect por los suelos arenosos), but they are also harbors that dan frutos, las nueces de coco, cuyos USOS are multiple. The más conocido is obviously the alimentario, which embraces both the fresh lung of the fruto as su \"leche\". Pero de las nueces de coco se puede obtener también un aceite vegetal, con la cáscara de las nueces de coco se puede producir coir (a textile fiber used to make cuerdas y alfombras), mientras that with the linfa is elaborated the conocido as \"palm wine\".",
            "https://universopalmeras.com/wp-content/uploads/2020/04/cocoteros-scaled.jpg",
            "Italia",
            "Coconut Palm")

        firebaseCompadre.setTreeLanguage(arbol3, "EN", "Palmera Cocotera")

        val arbol4 = DataTree(
            "-350kg",
            482,
            "The spiny oak is a evergreen shrub and usually reaches about 3 meters in height. Over time, some of the flowers form the characteristic fruits of the spiny oak. Its variety extends across almost the entire Mediterranean region. It is mainly cultivated for its fruits, which can be eaten roasted, for example.",
            "https://p4.wallpaperbetter.com/wallpaper/585/275/692/forest-mist-fog-misty-wallpaper-preview.jpg",
            "Italia",
            "Roure espinós")

        firebaseCompadre.setTreeLanguage(arbol4, "EN", "Roure espinós")

        val product1 = DataRecycleProduct(
            "8412771104856",
            "Milk",
            50,
            "https://firebasestorage.googleapis.com/v0/b/recolifter.appspot.com/o/recycleProducts%2Fleche.png?alt=media&token=0a7099a3-d373-40b6-bf91-97ed760a9e07",
            null
        )
        firebaseCompadre.setProductsScanLanguage(product1, "EN", product1.scanCode)
    }
}