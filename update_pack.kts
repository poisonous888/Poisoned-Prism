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






//
//val zipFile = selectedFile
//if (zipFile == null || !zipFile.exists()) {
//    IO.println("No zip file selected or file does not exist. Exiting.")
//    return
//}
//
//val currentDir = File(System.getProperty("user.dir"))
//println("Working directory: ${currentDir.absolutePath}")
//
//// Open the Zip file
//ZipFile(zipFile!!).use { zip ->
//    val targetSubdir = "assets/hypixel_skyblock/items/item"
//    val targetEntries = zip.entries().asSequence()
//        .filter { it.name.startsWith(targetSubdir) && !it.isDirectory }
//        .toList()
//
//    // 2. Check files in zip against files in the current directory
//    val zipEntriesMap = mutableMapOf<String, java.util.zip.ZipEntry>()
//    for (entry in targetEntries) {
//        val relativePath = entry.name.removePrefix("$targetSubdir/")
//        if (relativePath.isNotEmpty()) {
//            zipEntriesMap[relativePath] = entry
//            val localFile = File(currentDir, relativePath)
//
//            // If there's a file in the zip that's not in the current directory -> add the file
//            if (!localFile.exists()) {
//                localFile.parentFile?.mkdirs()
//                zip.getInputStream(entry).use { input ->
//                    Files.copy(input, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
//                }
//                println("Added missing file from zip: ${localFile.path}")
//            }
//        }
//    }
//
//    // Check if there's a file in the current directory missing from the zip
//    val localItemDir = File(currentDir, targetSubdir)
//    if (localItemDir.exists()) {
//        localItemDir.walkTopDown().filter { it.isFile }.forEach { localFile ->
//            val relativePath = localFile.toRelativeString(localItemDir)
//            if (!zipEntriesMap.containsKey(relativePath)) {
//                // Add a file with the same name except with .txt extension
//                val txtFile = File(localFile.parentFile, "${localFile.nameWithoutExtension}.txt")
//                val folderPath = localFile.parentFile.absolutePath
//                txtFile.writeText("TODO remove $folderPath ${localFile.name}")
//                println("Created TODO text file for missing zip counterpart: ${txtFile.path}")
//            }
//        }
//    }
//
//    // 3. Regex check every file in assets/hypixel_skyblock/items/item
//    val regex = Regex("hypixel_skyblock:([a-z_]+/)+([a-z_]+)")
//    val textureSourceDir = File(currentDir, "assets/hypixel_skyblock/textures/item")
//    val textureDestDir = File(currentDir, "assets/psnprsm/txtures/rem_item")
//    textureDestDir.mkdirs()
//
//    val evaluatedFiles = localItemDir.takeIf { it.exists() }?.walkTopDown()?.filter { it.isFile } ?: return@use
//
//    for (file in evaluatedFiles.iterator()) {
//        val content = file.readText()
//        val matches = regex.findAll(content)
//        for (match in matches) {
//            val group2 = match.groups[2]?.value
//            if (group2 != null) {
//                // Search in assets/hypixel_skyblock/textures/item for a file whose name matches group2
//                if (textureSourceDir.exists()) {
//                    textureSourceDir.walkTopDown().filter { it.isFile && it.nameWithoutExtension == group2 }.forEach { textureFile ->
//                        val destFile = File(textureDestDir, textureFile.name)
//                        Files.copy(textureFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
//                        println("Copied texture '${textureFile.name}' to ${destFile.path}")
//                    }
//                }
//            }
//        }
//    }
//}
//
//println("Script execution completed successfully!")