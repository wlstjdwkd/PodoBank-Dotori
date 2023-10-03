import React, { useState } from "react";
import { useEffect } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Image,
  Animated,
} from "react-native";
import { useDispatch, useSelector } from "react-redux";

export default function SavingCompleteScreen({ navigation, route }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외

  const [swingAnimate] = useState(new Animated.Value(0))


  useEffect(() => {
    Animated.sequence([
      Animated.timing(swingAnimate, {
        toValue: 45,
        duration: 300,
        useNativeDriver: true,
      }),
      Animated.timing(swingAnimate, {
        toValue: -45,
        duration: 300,
        useNativeDriver: true,
      }),
      Animated.timing(swingAnimate, {
        toValue: 0,
        duration: 300,
        useNativeDriver: true,
      }),
    ]).start();
  }, []);

  return (
    <View style={styles.container}>
      <View style={styles.centerContainer}>
        <Image
          style={styles.centerImage}
          source={require("../../assets/images/Hamster/LoveHamster.png")}
          // source={require("../../assets/images/kakao.png")}
        />
        {/* <Text style={styles.boldText}>" {name} "</Text> */}
        <Text style={styles.boldText}>저축에 성공하셨습니다.</Text>
        <Text style={styles.regularText}>도토리 1개를 드릴게요!</Text>
      </View>

      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          navigation.navigate("RewardScreen")
        }}
      >

        {/* 도토리 확인할 수 있는 페이지로 넘김 */}
        <Text style={styles.buttonText}>도토리 확인하기</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "space-between",
    padding: 40,
    backgroundColor: "white",
  },
  centerContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  centerImage: {
    width: 200, // 이미지의 너비
    height: 200, // 이미지의 높이
    marginBottom: 40,
  },
  boldText: {
    fontWeight: "bold",
    fontSize: 30,
    marginBottom: 10,
  },
  regularText: {
    fontSize: 25,
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
});
