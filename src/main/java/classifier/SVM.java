package classifier;
/**
 * Created by qiujiarong on 26/12/2017.
 */
import entity.Feature;
import featureExtractor.FeatureHandler;
import featureExtractor.Loader;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import java.util.Vector;

public class SVM {
    private static void SVM(Vector<Feature> train, Vector<Feature> test){
        svm_node[][] train_data=new svm_node[train.size()][];
        double[] train_labels = new double[train.size()];

        int idx=0;
        for(Feature feature:train){
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

        svm_problem problem = new svm_problem();
        problem.l = length;
        problem.x = train_data;
        problem.y = train_labels;


        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.LINEAR;
        param.cache_size = 100;
        param.eps = 0.00001;
        param.C = 1;


        if (svm.svm_check_parameter(problem, param) == null)
            System.out.println("Done checking SVM params...");

        svm_model model = svm.svm_train(problem, param);
        System.out.println("Done training the SVM model...");

        svm_node[][] test_data = new svm_node[test.size()][];
        double[] test_labels = new double[test.size()];
        idx=0;
        for(Feature feature:test){
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
        for(int i=0; i<test_data.length; i++){
            double result=svm.svm_predict(model, test_data[i]);
            if(result==test_labels[i])
                correct++;
//            System.out.println("predict "+result+" annotation "+labels[i]+" text "+features.get(i).getText());
        }

        System.out.println(correct+" "+test_data.length);

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Loader.load();
        SVM(FeatureHandler.getFeature(true),FeatureHandler.getFeature(false));
    }
}
