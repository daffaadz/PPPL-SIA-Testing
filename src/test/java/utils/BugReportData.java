package utils;

import java.util.List;

public class BugReportData {
    private String title;
    private String description;
    private String platform;
    private List<String> stepsToReproduce;
    private String expectation;
    private String priority;
    private String severity;
    private String attachmentPath;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public List<String> getStepsToReproduce() { return stepsToReproduce; }
    public void setStepsToReproduce(List<String> stepsToReproduce) { this.stepsToReproduce = stepsToReproduce; }

    public String getExpectation() { return expectation; }
    public void setExpectation(String expectation) { this.expectation = expectation; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getAttachmentPath() { return attachmentPath; }
    public void setAttachmentPath(String attachmentPath) { this.attachmentPath = attachmentPath; }
}
