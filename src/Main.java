import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Main extends Application {

    private int cevap;
    private int kalanDeneme = 6;
    private int kalanSure = 30;
    private Timeline timeline;

    @Override
    public void start(Stage stage) {
        Label infoLabel = new Label("1 ile 100 arasında bir sayı tahmin edin.");
        TextField textField = new TextField();
        Button guessButton = new Button("Tahmin Et");
        Label feedbackLabel = new Label();
        Label triesLabel = new Label("Kalan Deneme: " + kalanDeneme);
        Label timerLabel = new Label("Kalan Süre: " + kalanSure + " sn");

        VBox root = new VBox(10, infoLabel, textField, guessButton, feedbackLabel, triesLabel, timerLabel);
        root.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        cevap = (int) (Math.random() * 100) + 1;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            kalanSure--;
            timerLabel.setText("Süre: " + kalanSure + " sn");
            if (kalanSure <= 0)
                endGame(feedbackLabel, textField, guessButton, false);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        guessButton.setOnAction(e -> {
            if (kalanDeneme <= 0 || kalanSure <= 0) return;

            int tahmin;
            try {
                tahmin = Integer.parseInt(textField.getText());
            } catch (NumberFormatException ex) {
                feedbackLabel.setText("Geçerli sayı girin.");
                return;
            }

            if (tahmin < 1 || tahmin > 100) {
                feedbackLabel.setText("1-100 arası sayı girin.");
                return;
            }

            kalanDeneme--;
            triesLabel.setText("Kalan Deneme: " + kalanDeneme);

            if (tahmin == cevap) {
                endGame(feedbackLabel, textField, guessButton, true);
            } else {
                if (tahmin < cevap)
                    feedbackLabel.setText("Çok düşük!");
                else
                    feedbackLabel.setText("Çok yüksek!");

                if (kalanDeneme == 0)
                    endGame(feedbackLabel, textField, guessButton, false);
            }

            textField.clear();
        });

        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("Sayı Tahmin");
        stage.show();
    }

    private void endGame(Label feedback, TextField input, Button button, boolean won) {
        timeline.stop();
        input.setDisable(true);
        button.setDisable(true);
        feedback.setText(won ? "Tebrikler, Kazandınız!" : "Kaybettiniz! Cevap: " + cevap);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
