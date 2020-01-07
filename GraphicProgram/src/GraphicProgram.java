import java.awt.EventQueue;

public class GraphicProgram {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new Frame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
