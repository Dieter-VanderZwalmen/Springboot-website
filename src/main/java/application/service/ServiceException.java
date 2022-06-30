package application.service;

public class ServiceException extends RuntimeException {
    private String action;
    private Integer action1;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ServiceException(String action, String message) {
        super(message);
        this.action = action;
    }

    public ServiceException(Integer action1, String message) {
        super(message);
        this.action1 = action1;
    }
}
