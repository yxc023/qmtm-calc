package com.yangxiaochen.app.calc.calc;

import com.mathworks.toolbox.javabuilder.MWException;
import com.yangxiaochen.app.calc.calc.vo.Param;
import com.yangxiaochen.app.calc.calc.vo.Result;
import edu.ytu.qm.QuantitativeModel;
import edu.ytu.tm.TransportModel;

/**
 * Created by yangxiaochen on 15/12/27.
 */
public class Calculator {
    public static final String Model_AMA = "AMA";
    public static final String Model_PCI2014 = "PCI2014";
    public static final String Model_PCI2015 = "PCI2015";

    public Result calc(Param param, String model) throws MWException {
        double x = param.x; // 氯离子含量 输入
        double a = param.a; // 钢筋腐蚀电流值 输入

        // 钢筋界面区浓度
        QuantitativeModel qm = new QuantitativeModel();

        Object[] r = null;
        switch (model) {
            case Model_AMA: {
                r = qm.QuantitativeAMA(1,x,a);
                break;
            }
            case Model_PCI2014: {
                r = qm.QuantitativePCI2014(1,x,a);
                break;
            }
            case Model_PCI2015: {
                r = qm.QuantitativePCI2015(1,x,a);
            }
        }


        double y = Double.parseDouble(r[0].toString());

        double q = param.q; // 保护层厚度 输入
        double g = param.g; // 孔隙率 输入
        double z = param.z; // 湿度 输入
        double h = y; // 公式求出钢筋界面区浓度

        TransportModel tm = new TransportModel();

        switch (model) {
            case Model_AMA: {
                r = tm.TransportModelAMA(2,q,g,z,h);
                break;
            }
            case Model_PCI2014: {
                r = tm.TransportModelPCI2014(2,q,g,z,h);
                break;
            }
            case Model_PCI2015: {
                r = tm.TransportModelPCI2015(2,q,g,z,h);
                break;
            }
        }

        double r1 = Double.parseDouble(r[0].toString()); // 需要涂覆阻锈剂
        double r2 = Double.valueOf(r[1].toString()); // 分几次涂抹
        Result result = new Result(h,r1,r2);
        return result;
    }


    public static void main(String[] args) throws MWException {
        Param param = new Param();

        param.a = 0.1;
        param.g = 0.0446;
        param.x = 0.083;
        param.q = 45;
        param.z = 0.0001;

        new Calculator().calc(param,Model_PCI2015);

    }
}
