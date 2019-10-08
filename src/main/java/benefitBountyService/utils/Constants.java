package benefitBountyService.utils;

public class Constants {

    public static final String NO = "N";
    public static String EMPTY = "";
    public static String YES = "Y";
    public static String CR_STATUS = "Created";
    
    public enum STATUS {
        CREATED("Created"), COMPLETED("Completed"), IN_PROGRESS("In Progress"), SUBMITTED("Submitted"), APPROVED("Approved"), REJECTED("Rejected"), ON_HOLD("On Hold");

        private String status;

        private STATUS (String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    public enum ROLES {
        ADMIN("Admin"), STAKEHOLDER("Stakeholder"), APPROVER("Approver"), VOLUNTEER("Volunteer");

        private String role;

        private ROLES (String role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return role;
        }
    }
}
