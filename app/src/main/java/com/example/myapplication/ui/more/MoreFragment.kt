package com.example.myapplication.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_more.*


@AndroidEntryPoint
class MoreFragment : Fragment() {

    private lateinit var moreViewModel: MoreViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        moreViewModel =
                ViewModelProvider(this).get(MoreViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_more, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).needDrawer(false)
        activity?.setTitle("更多")

        cardGit.setOnClickListener {
            val uri: Uri = Uri.parse("https://github.com/hiki-long/BUPTAndroid")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        cardQq.setOnClickListener {

            joinQQGroup("3xQpjkVM213WUYhj1sdNIxj3rmnDfJP1")
        }
    }

    /****************
     *
     * 发起添加群流程。群号：安卓奥利给(782262533) 的 key 为： 3xQpjkVM213WUYhj1sdNIxj3rmnDfJP1
     * 调用 joinQQGroup(3xQpjkVM213WUYhj1sdNIxj3rmnDfJP1) 即可发起手Q客户端申请加群 安卓奥利给(782262533)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
//     ******************/
//    public boolean joinQQGroup(String key) {
//        Intent intent = new Intent();
//        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
//        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        try {
//            startActivity(intent);
//            return true;
//        } catch (Exception e) {
//            // 未安装手Q或安装的版本不支持
//            return false;
//        }
//    }
    public fun joinQQGroup (key : String) :Boolean{
        val intent = Intent()
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent);
            true;
        } catch (e:Exception) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(context,"未安装qq",Toast.LENGTH_SHORT).show()
            false;
        }
    }
}
