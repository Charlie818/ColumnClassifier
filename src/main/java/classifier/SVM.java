package classifier;
/**
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

    public static void SVM(Vector<Feature> train_features,Vector<Feature> test_features){
        svm_node[][] train_data=new svm_node[train_features.size()][];
        double[] train_labels = new double[train_features.size()];
        int idx=0;
        for(Feature feature:train_features){
            Vector<Double> vec=feature.getVec();
            svm_node[] ps= new svm_node[vec.size()];
            for(int i=0;i<vec.size();i++){
                svm_node p = new svm_node();
                p.index=idx;
                p.value=vec.get(i);
                ps[i]=p;
            }
            train_data[idx]=ps;
            train_labels[idx]=feature.getLabel();
            idx++;
        }

        int length=idx;
        //定义svm_problem对象
        svm_problem problem = new svm_problem();
        problem.l = length; //向量个数
        problem.x = train_data; //训练集向量表
        problem.y = train_labels; //对应的lable数组

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


        svm_node[][] test_data=new svm_node[test_features.size()][];
        double[] test_labels = new double[test_features.size()];
        idx=0;
        for(Feature feature:test_features){
            Vector<Double> vec=feature.getVec();
            svm_node[] ps= new svm_node[vec.size()];
            for(int i=0;i<vec.size();i++){
                svm_node p = new svm_node();
                p.index=idx;
                p.value=vec.get(i);
                ps[i]=p;
            }
            test_data[idx]=ps;
            test_labels[idx]=feature.getLabel();
            idx++;
        }
        int correct=0;
        for(int i=0;i<test_data.length;i++){
            double result=svm.svm_predict(model, test_data[i]);
            if(result==test_labels[i])correct++;
//            System.out.println("predict "+result+" annotation "+labels[i]+" text "+features.get(i).getText());
        }
        //预测测试数据的lable
        System.out.println(correct+" "+test_data.length);

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //定义训练集点a{10.0, 10.0} 和 点b{-10.0, -10.0}，对应lable为{1.0, -1.0}
        SVM(FeatureHandler.getFeature(true),FeatureHandler.getFeature(false));
    }
}
