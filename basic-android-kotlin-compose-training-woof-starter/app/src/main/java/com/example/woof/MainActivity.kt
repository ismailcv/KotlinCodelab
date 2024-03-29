package com.example.woof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.woof.data.Dog
import com.example.woof.data.dogs
import com.example.woof.ui.theme.WoofTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WoofTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    WoofApp()
                }
            }
        }
    }
}

/**
 * Composable that displays an app bar and a list of dogs.
 * Burada dogs dediği Dog.kt sınıfından aldığı listOf listesi
 */
@Composable
fun WoofApp() {/*
    Scaffold (iskelet) ekledik. ve bunu content padding it ile
    diğer şeylerle üst üste gelmesini engelledik.
    Scaffold da topBar ekledik ve onun için oluşturduğumuz
    woofTopAppBar composable'nı ekledik
     */
    Scaffold(topBar = {
        WoofTopAppBar()
    }) { it ->
        LazyColumn(contentPadding = it) {
            items(dogs) {
                DogItem(
                    dog = it,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}

/**
 * Composable that displays a list item containing a dog icon and their information.
 *
 * @param dog contains the data that populates the list item
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogItem(
    dog: Dog, modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        //Butonun genişletme işlemini default olarak kapalıya ayarladık
        var expanded by remember { mutableStateOf(false) }
        /*Bir colum oluşturduk üst satırda köpeğin bilgileri var
        alt satırda köpeklerin hobby bilgisi var.
        Buradaki column'a bir animation(animasyon) ekliyoruz.
        Ekleyeceğimiz bu animasyonun ismi spring animation
        çekip bıraktığımızda eski haline geri dönüyor.
        eski haline dönerken ekstra sıçrama olmasın diye noBouncy'i ekledik
        yay animasyonunu biraz daha sert hale getirmek için medium seviyeyi seçtik
        */
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            ) {
                DogIcon(dog.imageResourceId)
                DogInformation(dog.name, dog.age)
                //aşağı açma butonu ile resmin arasına boşluk bıraktık.
                Spacer(modifier = Modifier.weight(1f))
                //card'ın içine icon ve köpek bilgisinin yanına ekledik
                //Butona basıldığında durumunu değiştiriyoruz.
                DogItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded})
            }
            //Eğer expand(Genişletilirse) hobby gözükecek
            if (expanded) {
                DogHobby(
                    dog.hobbies, modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small),
                        end = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
        }
    }
}

/**
 * Composable that displays a photo of a dog.
 *
 * @param dogIcon is the resource ID for the image of the dog
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogIcon(
    @DrawableRes dogIcon: Int, modifier: Modifier = Modifier
) {
    //image'i clip ile shape'ten small dedik
    //sonrasında contentScale ile tam yuvarlak hale getirdik
    Image(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.Crop,
        painter = painterResource(dogIcon),

        // Content Description is not needed here - image is decorative, and setting a null content
        // description allows accessibility services to skip this element during navigation.

        contentDescription = null
    )
}

/**
 * Composable that displays a dog's name and age.
 *
 * @param dogName is the resource ID for the string of the dog's name
 * @param dogAge is the Int that represents the dog's age
 * @param modifier modifiers to set to this composable
 * burada stringRes dememizin sebebi o değeri text'e verecepimiz için.
 */
@Composable
fun DogInformation(
    @StringRes dogName: Int, dogAge: Int, modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(dogName),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        )
        Text(
            text = stringResource(R.string.years_old, dogAge),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/*
burada uygulama için topbar yaptık.
Topbar'ı sayfaya ortaladık. İçine bir image ve bir text ekledik

 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            //row'un içindeki cisimleri ortaladık.
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_size))
                        .padding(dimensionResource(id = R.dimen.padding_small)),
                    painter = painterResource(R.drawable.ic_woof_logo),

                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }, modifier = modifier
    )
}

/*
Çubuğa basıldığında aşağı doğru açılan expand more işlevini yapacağız
Açılıp açılmadığını kontrol etmek için expanded değişkenini
basılıp basılmadığını kontrol etmek içinde onClick fonksiyonunu kullanıyoruz
 */
@Composable
private fun DogItemButton(
    expanded: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    //Aşşağı indirme görseline tıkladığında aşağı indirme işlemi yapsın diye
    //icon button'u ekledik
    IconButton(
        onClick = onClick, modifier = modifier
    ) {
        //Icon ekledik açıkalamsını girdik rengini girdik.
        //tıklanabilir öğeyi genişletmek için expnad more'u ekledik.
        //Eğer sayfa genişletildiyse açıldıysa farklı icon genişletilmediyse farklı icon
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

/*
Köpeklerin hobilerin için oluşturduğum composable.
2 adet text var birisinde hakkında yazacak
diğerkinde ise o köpeğe ait hobby bulunacak.
 */
@Composable
fun DogHobby(
    @StringRes dogHobby: Int, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.about), style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(dogHobby), style = MaterialTheme.typography.labelSmall
        )
    }
}

/**
 * Composable that displays what the UI of the app looks like in light theme in the design tab.
 */
@Preview
@Composable
fun WoofPreview() {
    WoofTheme(darkTheme = false) {
        WoofApp()
    }
}

//Dark theme için bir preview ekliyoruz.
//Bu sayede hem aydınlık hemde karanlık ekran için theme oluyor
@Preview
@Composable
fun WoofDarkThemePreview() {
    WoofTheme(darkTheme = true) {
        WoofApp()
    }
}