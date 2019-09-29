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
    fun readCSVFile(name: String): ArrayList<ParkInfo>
}

class RecommendMenuUseCaseImpl(private val assets: AssetManager) : RecommendMenuUseCase {

    override fun readCSVFile(name: String): ArrayList<ParkInfo> {
        return when (name) {
            MenuType.PARK.fileName -> {
                readCsvFile(name)
            }
            else -> {
                readCsvFile(name)
            }
        }
    }

    private fun readCsvFile(fileName: String): ArrayList<ParkInfo> {

        val menuList: ArrayList<ParkInfo> = arrayListOf()

        val inputStram = assets.open(fileName)

        val bufferedReader = BufferedReader(
            InputStreamReader(inputStram, Charset.forName("x-windows-949"))
        )

        var line: String = ""

        bufferedReader.read()

        while ((bufferedReader.readLine()) != null && menuList.size<30) {
            line = bufferedReader.readLine()

            val columns = line.split(",")

            val menuInfo =
                ParkInfo(
                    columns[0],
                    columns[1],
                    columns[2],
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
                    columns[16],
                    0f
                )
            menuList.add(menuInfo)
        }

        return menuList
    }

    private fun getAddr(string: String): String {
        var result: String = ""

        val myPattern = Pattern.compile("\"(.*?)\",?")
        val myMatcher = myPattern.matcher(string)

        if (myMatcher.find()) {
            result = myMatcher.group(1)
        }
        return result
    }
}