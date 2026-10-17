import java.io.File
import java.lang.reflect.Executable
import kotlin.system.exitProcess

start()
fun start(){
    val oxy:String=File(System.getProperty("user.dir")).subdir("oxypng").listFiles()?.get(0)?.path?:{
        IO.println("Couldn't find oxypng zip")
        exitProcess(1)
    }.toString()
    ProcessBuilder(oxy,"-o max").start()
}
fun File.subdir(child:String):File{
    val index=this.list()?.indexOf(child)?:-1
    if(index==-1){
        IO.println("Unable to locate $child folder")
        exitProcess(1)
    }
    return this.listFiles()?.get(index)!!
}