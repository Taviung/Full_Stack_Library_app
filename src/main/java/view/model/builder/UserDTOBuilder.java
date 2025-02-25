package view.model.builder;

import view.model.UserDTO;

public class UserDTOBuilder {
    private String username;

    public UserDTOBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserDTO build() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(this.username);
        return userDTO;
    }
}
