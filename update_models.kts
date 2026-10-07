import java.io.File
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.FileReader
import java.io.Reader
import kotlin.system.exitProcess

val dir=File(System.getProperty("user.dir"))
start()
fun start(){
    IO.println("starting model verifier")
    val itemModelsFolder=dir.subdir("assets").subdir("hypixel_skyblock").subdir("items").subdir("item")
    val itemModels=ArrayList<File>()
    itemModelsFolder.recursiveGet(itemModels)
    IO.println("Found ${itemModels.size} item models to check")
    
    //--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//
    
    for(file in itemModels){
        val reader=FileReader(file)
        val str=reader.readAllAsString()
        
    }
    
    
    
    
    
    
    
    
    
}
fun File.subdir(child:String):File{
    val index=this.list()?.indexOf(child)?:-1
    if(index==-1){
        IO.println("Unable to locate $child folder")
        exitProcess(1)
    }
    return this.listFiles()?.get(index)!!
}
fun File.recursiveGet(arr:ArrayList<File>){
    if(isDirectory){
        for(file in listFiles()!!){
            file.recursiveGet(arr)
        }
        return
    }
    arr.add(this)
}
fun parseJson(str:String):JsonNode{
    val test=JsonNode.Number
    
}
enum class JsonNode{
    Object{val objects=mutableMapOf<String,JsonNode>()},
    Array{val list=mutableListOf<JsonNode>()},
    String{var string=""},
    Number{var number:Number=1}
}