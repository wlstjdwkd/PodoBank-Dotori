import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Image,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";

export default function PurposeCreate1Screen({ navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>{state.user.grantType})
  const accessToken =  useSelector((state)=>{state.user.accessToken})
  const refreshToken =  useSelector((state)=>{state.user.refreshToken})
  const dispatch = useDispatch()
  // 그 외
  
  const [purposeInfo, setPurposeInfo] = useState({
    purposeName: "",
  });
  const [isValid, setIsValid] = useState(false);
  const handleNameChange = (text) => {
    setPurposeInfo((prev) => ({ ...prev, purposeName: text }));
    if (text.length > 0 && text.length <= 10) {
      setIsValid(true);
    } else {
      setIsValid(false);
    }
  };
  return (
    <View style={styles.container}>
      <HeaderComponent
        title="목표생성(1/3)"
        cancelNavi="PurposeScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <View style={styles.header}>
          <Text style={styles.title}>목표 설정하기</Text>
          <Text style={styles.subtitle}>달성할 목표를 입력해주세요.</Text>
          <View style={styles.imageView}>
            <Image
              style={styles.middleImage}
              source={require("../../assets/images/Hamster/PurposeCreateHamster.png")}
            ></Image>
          </View>

          <TextInput
            style={styles.input}
            onChangeText={handleNameChange}
            multiline={true}
          />
          <View style={styles.textRight}>
            <Text style={styles.instructionText}>
              10자 이내로 작성해주세요.
            </Text>
          </View>
        </View>

        <TouchableOpacity
          style={styles.button}
          onPress={() =>
            navigation.navigate("PurposeCreate2Screen", {
              purposeInfo: purposeInfo,
            })
          }
          //TODO: 풀기
          // disabled={!isValid}
        >
          <Text style={styles.buttonText}>다음</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "space-between",
    padding: 20,
    backgroundColor: "white",
  },
  innerContainer: {
    flex: 1,
    justifyContent: "space-between",
    padding: 20,
    backgroundColor: "white",
  },
  header: {
    flex: 1,
    // justifyContent: "center",
    // alignItems: "center",
    marginTop: 90,
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
  subtitle: {
    color: "#7B7B7B",
    marginBottom: 30,
  },
  input: {
    width: "100%",
    height: 50,
    backgroundColor: "#D9D9D920",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 10,
    // textAlign: "center",
  },
  button: {
    height: 40,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
  imageView: {
    justifyContent: "center",
    alignItems: "center",
    marginTop: 30,
    marginBottom: 40,
  },
  middleImage: {
    // justifyContent: "center",
    // alignItems: "center",
    width: 150,
    height: 150,
  },
  textRight: {
    alignItems: "flex-end",
    marginTop: 10,
  },
  instructionText: {
    color: "#A9A9A9",
    fontSize: 12,
  },
});
