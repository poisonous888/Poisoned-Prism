import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.system.exitProcess

start()
fun start(){
    val dur=File(System.getProperty("user.dir"))
    val oxy:String=dur.subdir("oxypng").listFiles()?.get(0)?.path?:{
        IO.println("Couldn't find oxypng zip")
        exitProcess(1)
    }.toString()
    val toCheck=dur.subdir("assets").path
    println(toCheck)
    val process=ProcessBuilder(oxy,"-r","-o","max","--strip","all",toCheck).start()
    val output=BufferedReader(InputStreamReader(process.inputStream))
    var line: String? = output.readLine()
    
    while (line != null) {
        println(line)
        line = output.readLine()
    }
    println("Exit code: ${process.waitFor()}")
}
fun File.subdir(child:String):File{
    val index=this.list()?.indexOf(child)?:-1
    if(index==-1){
        println("Unable to locate $child folder")
        exitProcess(1)
    }
    return this.listFiles()?.get(index)!!
}