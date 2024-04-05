package com.example.shoppinglistapp


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


data class ShoppingItem(
    val id:Int,
    var name:String,
    var quantity:Int,
    var isEditing:Boolean=false
)

@Composable
fun ShoppingListApp(){
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember{ mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    Column(
        modifier= Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {showDialog=true},
            modifier= Modifier.align(Alignment.CenterHorizontally)
        ){
            Text("Add Item")
        }
        LazyColumn(
            modifier= Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems){
                item ->
                println("displaying item")
                if(item.isEditing){
                    ShoppingItemEditor(item = item, onEditComplete = {
                        editedName, editedQuantity ->
                        sItems=sItems.map { it.copy(isEditing = false) }
                        val editedItem=sItems.find{it.id==item.id}
                        editedItem?.let{
                            it.name=editedName
                            it.quantity=editedQuantity
                        }
                    })
                }else
                {
                    ShoppingListItem(item = item, onEditClick = {
                        println("Clicked on Edit!!")
                        sItems=sItems.map{it.copy(isEditing = it.id==item.id)}
                    }, onDeleteClick = {
                        sItems=sItems-item
                    })
                }
            }
        }
    }
    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog=false },
            confirmButton = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick = {
                        if(itemName.isNotBlank()){
                            val newItem=ShoppingItem(
                                id= sItems.size+1,
                                name=itemName,
                                quantity = itemQuantity.toInt()
                            )
                            sItems=sItems+newItem
                            println("After add")
                            println(sItems)
                            showDialog=false
                            itemName=""
                            itemQuantity=""
                        }
                    }) {
                        Text(text = "Add")
                    }
                    Button(onClick = {
                        showDialog=false
                    }) {
                        Text("Cancel")
                    }
                }
            },
            title = { Text(text = "Add Shopping Item!")},
            text = {
            //we can use any kind of composables in here
            Column() {
                OutlinedTextField(
                    value =itemName,
                    onValueChange = {itemName=it},
                    singleLine= true,
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
                OutlinedTextField(
                    value =itemQuantity,
                    onValueChange = {itemQuantity=it},
                    singleLine= true,
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
            }
        })
    }
}


@Composable
fun ShoppingItemEditor(item: ShoppingItem,onEditComplete:(String,Int) -> Unit){
    var editedName by remember{ mutableStateOf(item.name) }
    var editedQuantity by remember{ mutableStateOf(item.quantity.toString()) }
    var isEditing by remember{ mutableStateOf(item.isEditing) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column() {
            BasicTextField(
                value = editedName,
                onValueChange = {editedName=it},
                singleLine=true,
                modifier= Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = {editedQuantity=it},
                singleLine=true,
                modifier= Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing=false
            onEditComplete(editedName,editedQuantity.toIntOrNull() ?:1)
        }) {
            Text(text = "Save")
        }
    }

}

@Composable
fun ShoppingListItem(
    item:ShoppingItem,
    onEditClick:()->Unit,
    onDeleteClick:()->Unit,
){
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(
            border = BorderStroke(2.dp, Color(0XFF018786)),
            shape = RoundedCornerShape(20)
        ),
        horizontalArrangement = Arrangement.SpaceBetween){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.padding(8.dp)
        ){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}