import java.io.File
import java.util.zip.ZipFile
import kotlin.system.exitProcess

val dir=File(System.getProperty("user.dir"))
start()
fun start(){
    IO.println("starting auto update")
    val mcFolder:File=dir.parentFile.parentFile
    val downloads=mcFolder.subdir("downloads")
    val downloadContents=downloads.listFiles()
    if(downloadContents.isNullOrEmpty()){
        IO.println("Cannot find latest pack, let hypixel download the latest pack")
        exitProcess(1)
    }
    val checkList = arrayListOf<File>()
    for(file in downloadContents){
        if(file.isDirectory){
            val content=file.listFiles()
            checkList.addAll(content!!)
        }
    }
    IO.println("Found ${checkList.size} file(s) to check against")
    if(checkList.size>1){
        IO.println("Cannot determine latest pack, delete minecraft downloads folder and let hypixel redownload pack")
        exitProcess(1)
    }
    if(checkList.isEmpty()){
        IO.println("Cannot find latest pack, let hypixel redownload pack")
        exitProcess(1)
    }
    
    //--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//
    
    val items=dir.subdir("assets").subdir("hypixel_skyblock").subdir("items").subdir("item")
    val recursiveItems=ArrayList<File>()
    items.recursiveGet(recursiveItems)
    val hash=HashSet<String>()
    for(file in recursiveItems){
        val str=file.path.replace("\\","/")
        val start=str.indexOf("assets/hypixel_skyblock")
        val trimmed=str.substring(start)
        //IO.println(trimmed)
        hash.add(trimmed)
    }
    
    //--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//--//
    
    //checking each file for if it already exists in the pack
    IO.println("ADDITIONS:")
    val zip=ZipFile(checkList[0])
    for(entry in zip.entries()){
        if(entry.isDirectory)continue
        if(!entry.name.contains("hypixel_skyblock"))continue
        if(entry.name.contains(".json")){
            if(entry.name.contains("models"))continue
            hash.remove(entry.name)
            val file = File(dir,entry.name)
            if(file.exists())continue
            IO.println(entry.name)
            val inStream = zip.getInputStream(entry)
            val outStream = file.outputStream()
            inStream.copyTo(outStream)
        }
//        if(entry.name.contains(".png")){
//            //IO.println(entry.name)
//        }
    }
    IO.println("REMOVE:")
    for(item in hash){
        IO.println(item)
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