package hbs.com.picnic.view.recommend.usecase

import android.content.res.AssetManager
import hbs.com.picnic.data.model.MenuInfo
import hbs.com.picnic.data.model.ParkInfo
import hbs.com.picnic.utils.MenuType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.regex.Pattern


interface RecommendMenuUseCase {
    fun readCSVFile(name: String): List<Any>
}

class RecommendMenuUseCaseImpl(private val assets: AssetManager) : RecommendMenuUseCase {

    override fun readCSVFile(name: String): List<Any> {
        return when (name) {
            MenuType.PARK.fileName -> {
                readCsvFile(name)
            }
            else -> {
                readCsvFile(name)
            }
        }
    }

    private fun readCsvFile(fileName: String): MutableList<Any> {

        val isMarket: Boolean = fileName == MenuType.PARK.fileName

        val menuList: MutableList<Any> = mutableListOf()

        val inputStram = assets.open(fileName)

        val bufferedReader = BufferedReader(
            InputStreamReader(inputStram, Charset.forName("x-windows-949"))
        )

        var line: String = ""
        var addr: String = ""

        bufferedReader.read()

        while ((bufferedReader.readLine()) != null) {
            line = bufferedReader.readLine()

            addr = getAddr(line)

            line = line.replace("\"$addr\"", "")

            val columns = line.split(",")

            addr =
                if (columns[1].isEmpty()) addr
                else columns[1]

            val menuInfo =
                if(isMarket){
                    ParkInfo(
                        columns[0],
                        columns[2],
                        columns[1],
                        columns[3],
                        columns[4],
                        columns[5],
                        columns[6],
                        columns[7],
                        columns[8],
                        columns[9],
                        columns[10],
                        columns[11],
                        columns[12],
                        columns[13],
                        columns[14],
                        columns[15],
                        columns[16]
                    )
                }else{
                    MenuInfo(
                        columns[0],
                        addr,
                        columns[2],
                        columns[3],
                        columns[4]
                    )
                }

            menuList.add(menuInfo)
        }

        return menuList
    }

    fun getAddr(string: String): String {
        var result: String = ""

        val myPattern = Pattern.compile("\"(.*?)\",?")
        val myMatcher = myPattern.matcher(string)

        if (myMatcher.find()) {
            result = myMatcher.group(1)
        }
        return result
    }
}