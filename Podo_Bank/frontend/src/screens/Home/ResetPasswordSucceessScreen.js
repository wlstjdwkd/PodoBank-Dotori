import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  Alert,
} from "react-native";
import HeaderComponent from "../Header/HeaderScreen";

export default function SignupIdentityVerificationScreen({ navigation, route }) {
  const userID = route.params.userID
  const userPassword = route.params.password;

  return (
    <View style={styles.container}>
      <View style={styles.halfContainer}>
        {/* Header */}
        <HeaderComponent
          title="아이디 찾기"
          navigation={navigation}
        ></HeaderComponent>

        <View style={styles.showUserBoldText1}>
          <Text style={[styles.showUserBoldText2]}>
            {userID}님의 비밀번호는{'\n'}{userPassword}{'\n'}입니다.
          </Text>
        </View>

        <View style={{flex:0.2}}>
          <TouchableOpacity
            style={[
              styles.customButton,
            ]}
            onPress={() =>{
              navigation.navigate("LoginScreen")
            }}
            >
            <Text style={styles.buttonText}>로그인 페이지로</Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 20,
    paddingTop: 20,
    backgroundColor: "white",
  },
  halfContainer: {
    flex: 0.5,
    backgroundColor: "white",
  },
  boldText: {
    fontWeight: "bold",
    fontSize: 20,
    marginTop: 30,
    marginBottom: 30,
    marginRight: 30,
    marginLeft: 30,
  },
  inputText: {
    marginLeft: 30,
  },
  inputContainer: {
    marginTop: 10,
    justifyContent: "flex-start",
  },
  input: {
    borderWidth: 1,
    borderRadius: 10,
    paddingLeft: 10,
    marginTop: 10,
    marginRight: 30,
    marginLeft: 30,
    marginBottom: 30,
    height: 50,
    textAlignVertical: "center",
    elevation: 5,
    backgroundColor: "white",
  },
  customButton: {
    backgroundColor: "#A175FD",
    padding: 10,
    borderRadius: 5,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 10,
    marginRight: 30,
    marginLeft: 30,
    borderRadius: 5,
  },
  tinyCenterLinkText:{
    textAlign: 'center',
    margin: 10
  },
  showUserBoldText1:{
    flex:0.8, 
    justifyContent: 'center', 
    alignItems: 'center'
  },
  showUserBoldText2:{
    fontWeight: "bold",
    fontSize: 20,
    marginTop: 30,
    marginBottom: 30,
    marginRight: 30,
    marginLeft: 30,
    textAlign: 'center',
    lineHeight: 40
  },
  buttonText: {
    color: "white",
    fontSize: 16,
  },
});
