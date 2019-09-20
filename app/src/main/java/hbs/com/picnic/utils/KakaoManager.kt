package hbs.com.picnic.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ButtonObject
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import hbs.com.picnic.R

class KakaoManager(val context: Context) {
    fun sendMsg(msg: String, imgUrl: String) {
        val params = FeedTemplate
            .newBuilder(
                ContentObject.newBuilder(
                    context.getString(R.string.all_text_kakao_link_title),
                    imgUrl,
                    LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com").build()
                )
                    .setDescrption(msg)
                    .build()
            )
            .addButton(
                ButtonObject(
                    context.getString(R.string.all_text_kakao_link_title), LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("'https://developers.kakao.com")
                        .build()
                )
            )
            .build()

        val serverCallbackArgs = HashMap<String, String>()
        serverCallbackArgs["user_id"] = "no";
        serverCallbackArgs["product_id"] = "17334";

        KakaoLinkService.getInstance().sendDefault(
            context,
            params,
            serverCallbackArgs,
            object : ResponseCallback<KakaoLinkResponse>() {
                override fun onSuccess(result: KakaoLinkResponse?) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.all_text_kakao_link_on_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(errorResult: ErrorResult?) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.all_text_kakao_link_on_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("kakao error", errorResult?.errorMessage)
                }
            })
    }
}