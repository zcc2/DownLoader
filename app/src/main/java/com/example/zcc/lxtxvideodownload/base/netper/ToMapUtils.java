package com.example.zcc.lxtxvideodownload.base.netper;

import com.example.zcc.lxtxvideodownload.base.utils.AppUtils;
import com.pbrx.mylib.constant.LibConstant;
import com.pbrx.mylib.util.LogUtil;
import com.pbrx.mylib.util.SPUtils;
import com.pbrx.mylib.util.UserInfoBean;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ToMapUtils {

    /**
     * 实体对象转成Map
     * @param obj 实体对象
     * @return
     */
    public static Map<String, String> toParamMap(Object obj) {
        Map<String, String> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                UserInfoBean userInfoBean = (UserInfoBean) SPUtils.get(LibConstant.user_info,null);
                LogUtil.e("fieldname======",field.getName());
                if(userInfoBean !=null&&"empId".equals(field.getName())){
                    LogUtil.e("user_id======", userInfoBean.getEmployee().getId()+"");
                    map.put(field.getName(), userInfoBean.getEmployee().getId()+"");
                }
                if(AppUtils.checkStrsNoNull((String) field.get(obj))){
                    map.put(field.getName(), (String) field.get(obj));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
