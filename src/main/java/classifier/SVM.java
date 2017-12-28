package classifier; /**
 * Created by qiujiarong on 26/12/2017.
 */
import entity.Feature;
import featureExtractor.FeatureHandler;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import java.util.Vector;

public class SVM {

    public static void SVM(Vector<Feature> features){
        svm_node[][] data=new svm_node[features.size()][];
        double[] labels = new double[features.size()];
        int idx=0;
        for(Feature feature:features){
            Vector<Double> vec=feature.getVec();
            svm_node[] ps= new svm_node[vec.size()];
            for(int i=0;i<vec.size();i++){
                svm_node p = new svm_node();
                p.index=idx;
                p.value=vec.get(i);
                ps[i]=p;
            }
            data[idx]=ps;
            labels[idx]=feature.getLabel();
            idx++;
        }

        int length=idx;
        //定义svm_problem对象
        svm_problem problem = new svm_problem();
        problem.l = length; //向量个数
        problem.x = data; //训练集向量表
        problem.y = labels; //对应的lable数组

        //定义svm_parameter对象
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.LINEAR;
        param.cache_size = 100;
        param.eps = 0.00001;
        param.C = 1;

        //训练SVM分类模型
        System.out.println(svm.svm_check_parameter(problem, param)); //如果参数没有问题，则svm.svm_check_parameter()函数返回null,否则返回error描述。
        svm_model model = svm.svm_train(problem, param); //svm.svm_train()训练出SVM分类模型

        //定义测试数据点c
        svm_node pc0 = new svm_node();
        pc0.index = 0;
        pc0.value = -0.1;
        svm_node pc1 = new svm_node();
        pc1.index = -1;
        pc1.value = 0.0;
        svm_node[] pc = {pc0, pc1};

        int correct=0;
        for(int i=0;i<length;i++){
            double result=svm.svm_predict(model, data[i]);
            if(result==labels[i])correct++;
            System.out.println("predict "+result+" annotation "+labels[i]+" text "+features.get(i).getText());
        }
        //预测测试数据的lable
        System.out.println(correct+" "+length);

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //定义训练集点a{10.0, 10.0} 和 点b{-10.0, -10.0}，对应lable为{1.0, -1.0}
        SVM(FeatureHandler.getFeature());
    }
}
