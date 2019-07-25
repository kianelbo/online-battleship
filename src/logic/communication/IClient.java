package logic.communication;

public interface IClient {
    void onReject();
    void onStart(String opName);
}
