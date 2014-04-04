import com.fourtyblocksunder.abstention.editor.data.ClusterData;
import com.fourtyblocksunder.abstention.editor.data.GraphData;
import com.fourtyblocksunder.abstention.editor.data.ProjectData;
import com.google.gson.Gson;


public class GsonTest {

	public static void main(String[] args) {
		Gson gson = new Gson();
		ProjectData projectData = new ProjectData();
		GraphData graphData = new GraphData();
		ClusterData clusterData = new ClusterData();
		graphData.addCluster(clusterData);
		projectData.addGraphData(graphData);
		System.out.println(gson.toJson(projectData));
	}

}
