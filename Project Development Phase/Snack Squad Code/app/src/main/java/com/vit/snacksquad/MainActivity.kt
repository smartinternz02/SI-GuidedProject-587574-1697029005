package com.vit.snacksquad

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.shreyanshsingh21BCE10379.snacksquad.ui.theme.ExternshipTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExternshipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val context = LocalContext.current
    val databaseHelper = remember { UserDatabaseHelper(context) }
    val dataStore = remember { StoreUserName(context) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.3f
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 40.dp),
                fontFamily = FontFamily.Cursive
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person, contentDescription = "Username Icon"
                    )
                },
                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock, contentDescription = "Password Icon"
                    )
                },
                modifier = Modifier.padding(bottom = 20.dp)
            )

            if (error.isNotEmpty()) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                error = ""
            }

            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    val user = databaseHelper.getUserByUsername(username)
                    if (user != null && user.password == password) {
                        error = "Successfully logged in"
                        scope.launch {
                            dataStore.saveName(username)
                        }
                        navController.navigate("RestaurantList")
                    } else {
                        error = "Invalid username or password"
                    }

                } else {
                    error = "Please fill all fields"
                }
            }) {
                Text(text = "Login", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Sign Up",
                modifier = Modifier.clickable { navController.navigate("SignUpScreen") },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val context = LocalContext.current
    val databaseHelper = remember { UserDatabaseHelper(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.3f
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 40.dp),
                fontFamily = FontFamily.Cursive
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person, contentDescription = "Username Icon"
                    )
                },
                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email, contentDescription = "Email Icon"
                    )
                },
                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock, contentDescription = "Password Icon"
                    )
                },
                modifier = Modifier.padding(bottom = 20.dp)
            )

            if (error.isNotEmpty()) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                error = ""
            }

            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = {
                if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                    val user = User(
                        id = null,
                        firstName = username,
                        email = email,
                        password = password
                    )
                    databaseHelper.insertUser(user)
                    error = "User registered successfully"
                    navController.navigate("LoginScreen")
                } else {
                    error = "Please fill all fields"
                }
            }) {
                Text(text = "Sign Up", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantList(navController: NavController) {
    val restaurants = listOf("McDonald's", "KFC", "Taco Bell", "Burger King")
    val restaurantImages =
        listOf(R.drawable.mcdonalds, R.drawable.kfc, R.drawable.taco_bell, R.drawable.bk)
    val ratings = listOf("3.9", "4.3", "4.5", "4.1")
    val distance = listOf("1", "2", "5", "3")
    val timeToOrder = listOf("30", "25", "45", "35")
    Scaffold(topBar = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(60.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Home",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Restaurant List",
                modifier = Modifier.weight(5f),
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Cart",
                modifier = Modifier.weight(1f)
            )
        }
    }, content = {
        LazyColumn(contentPadding = it, content = {
            itemsIndexed(restaurants) { x, item ->
                Card(
                    onClick = { navController.navigate("Menu/" + restaurants[x]) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    elevation = CardDefaults.cardElevation(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
                    ) {
                        Image(
                            painter = painterResource(id = restaurantImages[x]),
                            contentDescription = "McDonald's",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.height(200.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.padding(
                                horizontal = 15.dp, vertical = 10.dp
                            )
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.weight(4f)
                            )
                            Badge(
                                modifier = Modifier.weight(1f), containerColor = Color(0xFF568940)
                            ) {
                                Text(
                                    text = ratings[x],
                                    textAlign = TextAlign.End,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Rating",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .height(30.dp)
                                        .padding(start = 1.dp)
                                )
                            }
                        }
                        Row(modifier = Modifier.padding(horizontal = 15.dp)) {
                            Icon(
                                imageVector = Icons.Filled.Place, contentDescription = "Distance"
                            )
                            Text(
                                text = distance[x] + " km away",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 15.dp, vertical = 15.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DateRange, contentDescription = "Time"
                            )
                            Text(
                                text = "Delivery in " + timeToOrder[x] + " mins",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                    }
                }
            }
        })
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(navController: NavController, restaurant: String) {
    val menuItems = when (restaurant) {
        "McDonald's" -> listOf("Chicken Burger", "Fries", "Veg Burger", "Cold Drinks", "Ice Cream")
        "KFC" -> listOf(
            "Chicken Popcorn",
            "Hot & Crispy Chicken",
            "Grilled Chicken",
            "Chicken Wings",
            "Chicken Burger"
        )

        "Taco Bell" -> listOf(
            "Crispy Chicken Wrap", "Veggie Taco", "Burrito", "Taco Mexican", "Chicken Taco"
        )

        "Burger King" -> listOf(
            "Cheese Chicken Whopper",
            "Veggie Burger",
            "Berry Blast",
            "Veggie Strips",
            "Boneless Wings"
        )

        else -> listOf("Chicken Burger", "Fries", "Veg Burger", "Cold Drinks", "Ice Cream")
    }
    val restaurantImage = when (restaurant) {
        "McDonald's" -> R.drawable.mcdonalds
        "KFC" -> R.drawable.kfc
        "Taco Bell" -> R.drawable.taco_bell
        "Burger King" -> R.drawable.bk
        else -> R.drawable.mcdonalds
    }
    val menuImages = listOf(
        R.drawable.chicken_burger,
        R.drawable.fries,
        R.drawable.veg_burger,
        R.drawable.cold_drinks,
        R.drawable.ice_cream
    )
    val price = listOf("₹120", "₹110", "₹100", "₹140", "₹150")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val noOfItemsInCart = remember { mutableIntStateOf(0) }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(60.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
            Text(
                text = restaurant,
                modifier = Modifier.weight(5f),
                style = MaterialTheme.typography.titleLarge
            )
            BadgedBox(badge = {
                Badge(
                    modifier = Modifier.offset(x = (-20).dp)
                ) {
                    Text(text = noOfItemsInCart.intValue.toString())
                }
            }, modifier = Modifier.weight(1f), content = {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart, contentDescription = "Cart"
                )
            })
        }
    }, content = {
        LazyColumn(contentPadding = it) {
            itemsIndexed(menuItems) { x, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = if (restaurant != "McDonald's") restaurantImage else menuImages[x]),
                            contentDescription = "Restaurant Image",
                            modifier = Modifier
                                .weight(2f)
                                .height(100.dp),
                            contentScale = ContentScale.FillWidth
                        )
                        Column(
                            modifier = Modifier
                                .weight(3f)
                                .padding(start = 10.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = item, style = MaterialTheme.typography.headlineSmall
                            )
                            Text(text = price[x], style = MaterialTheme.typography.labelLarge)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = menuItems[x] + " added to Cart",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                            noOfItemsInCart.intValue++
                        }) {
                            Text(
                                text = "Add to Cart",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Add to Cart"
                            )
                        }
                    }
                }
            }
        }
    }, bottomBar = {
        if (noOfItemsInCart.intValue > 0) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(60.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Items in cart: ${noOfItemsInCart.intValue}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(onClick = { navController.navigate("PaymentPage/$restaurant") }) {
                    Text(text = "Buy")
                }
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentPage(navController: NavController, restaurant: String?) {
    val context = LocalContext.current
    var address by remember { mutableStateOf("") }
    var cardNo by remember { mutableStateOf("") }
    var expDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var upi by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phNo by remember { mutableStateOf("") }
    var dialogOpen by remember { mutableStateOf(false) }
    val radioOptions = listOf("Cash", "Debit Card", "Credit Card", "UPI")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val dataStore = remember { StoreUserName(context) }
    val username = dataStore.getName.collectAsState(initial = "")
    val databaseHelper = remember { UserDatabaseHelper(context) }
    val user = databaseHelper.getUserByUsername(username.value!!)

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Fill your details") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                }
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it), verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 40.dp)
            ) {
                Text(
                    text = "Customer Details",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                OutlinedTextField(value = address, onValueChange = { address = it }, label = {
                    Text(
                        text = "Address"
                    )
                }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                ), singleLine = true, modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(value = phNo, onValueChange = { phNo = it }, label = {
                    Text(
                        text = "Phone Number"
                    )
                }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ), singleLine = true, modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(80.dp))
                Text(
                    text = "Payment Method",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                radioOptions.forEach { text ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                modifier = Modifier.padding(8.dp),
                                onClick = { onOptionSelected(text) }
                            )
                            Text(
                                text = text,
                                modifier = Modifier.padding(start = 10.dp),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
                if (selectedOption != "Cash") {
                    if (selectedOption == "Debit Card" || selectedOption == "Credit Card") {
                        Spacer(modifier = Modifier.height(80.dp))
                        Text(
                            text = "Enter $selectedOption Information",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        OutlinedTextField(
                            value = cardNo,
                            onValueChange = { if (it.length <= 16) cardNo = it },
                            label = {
                                Text(
                                    text = "$selectedOption Number"
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                capitalization = KeyboardCapitalization.Words
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = expDate,
                                onValueChange = { expDate = it },
                                label = {
                                    Text(
                                        text = "Expiration Date"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 10.dp)
                                    .weight(4f)
                            )
                            OutlinedTextField(
                                value = cvv,
                                onValueChange = { if (it.length <= 3) cvv = it },
                                label = {
                                    Text(
                                        text = "CVV"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.NumberPassword
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = {
                                Text(
                                    text = "Name on Card"
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Words
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else if (selectedOption == "UPI") {
                        Spacer(modifier = Modifier.height(80.dp))
                        Text(
                            text = "Enter UPI Information",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        OutlinedTextField(
                            value = upi,
                            onValueChange = { upi = it },
                            label = {
                                Text(
                                    text = "Enter UPI ID"
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp), horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                val dbAccess = OrderHelper()
                                dbAccess.placeOrder(
                                    user!!.firstName,
                                    user.email,
                                    phNo,
                                    address,
                                    selectedOption,
                                    restaurant!!
                                )
                            }
                            dialogOpen = true
                        },
                        contentPadding = PaddingValues(15.dp, 10.dp),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Submit",
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "Order",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
                if (dialogOpen) {
                    Dialog(onDismissRequest = { }) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            shadowElevation = 6.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Thank you for your Order!",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "Your food will arrive shortly.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(30.dp))
                                Button(onClick = {
                                    navController.popBackStack(
                                        "RestaurantList",
                                        false
                                    )
                                }) {
                                    Text(
                                        text = "Go back",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}