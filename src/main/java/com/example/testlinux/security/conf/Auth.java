package com.example.testlinux.security.conf;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class Auth {

    private Integer loginId;

    private Role role;

    private String rawRole;

    private boolean isAuthenticated;

    public Auth(Integer loginId, String role, boolean isAuthenticated){
        this.loginId = loginId;
        this.rawRole = role;
        this.role = Auth.Role.getRole(role);
        this.isAuthenticated = isAuthenticated;
    }

    public boolean isAuthenticated(){
        return isAuthenticated;
    }

    public boolean isFighter(){
        return role.isFighter();
    }

    public boolean isJudge(){
        return role.isJudge();
    }

    public boolean isOrganizer(){
        return role.isOrganizer();
    }

    public boolean isTrainer(){
        return role.isTrainer();
    }

    public boolean isOrgAdmin(){
        return "orgAdmin".equalsIgnoreCase(rawRole);
    }

    public enum Role {
        Fighter(List.of("Fighter", "member")),
        Judge(List.of("Referee", "judge")),
        Organizer(List.of("Organizer")),
        Trainer(List.of("Trainer", "sunsey")),
        Empty(List.of(""));

        private final List<String> roleNames;

        Role(List<String> roleNames){
            this.roleNames = roleNames;
        }

        public boolean isFighter(){
            return this.name().equals(Fighter.name());
        }

        public boolean isJudge(){
            return this.name().equals(Judge.name());
        }

        public boolean isTrainer(){
            return this.name().equals(Trainer.name());
        }

        public boolean isOrganizer(){
            return this.name().equals(Organizer.name());
        }

        public static Role getRole(String value){
            for(Role role : Role.values()){

                for(String roleName : role.roleNames){
                    if(roleName.equalsIgnoreCase(value)){
                        return role;
                    }
                }

            }
            return Empty;
        }

    }
}
