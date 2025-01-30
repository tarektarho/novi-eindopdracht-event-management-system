package nl.novi.event_management_system.exceptions;

public class FileDownloadException extends RuntimeException {
  public FileDownloadException(String message) {
    super(message);
  }

    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
