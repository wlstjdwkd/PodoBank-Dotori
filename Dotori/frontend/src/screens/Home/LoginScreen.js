import React from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  Image,
  StyleSheet,
} from "react-native";

export default function LoginScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <Image
        style={styles.image}
        source={require("../../assets/images/dotori_logo.png")}
      />

      <TextInput style={styles.input} placeholder="이메일" />
      <TextInput style={styles.input} placeholder="비밀번호" />

      <View style={styles.checkboxContainer}>
        <View style={styles.checkbox} />
        <Text style={{ color: "#878787" }}>아이디저장</Text>
      </View>

      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          navigation.navigate("MainPageScreen")
        }}
      >
        <Text style={styles.buttonText}>로그인</Text>
      </TouchableOpacity>

      <View style={styles.linksContainer}>
        <TouchableOpacity
          onPress={()=>{
            navigation.navigate("LoginScreen")
          }}
        >
          <Text style={styles.linkText}>아이디 찾기</Text>
        </TouchableOpacity>

        <Text style={styles.linkText}>|</Text>
        <TouchableOpacity
          onPress={()=>{
            navigation.navigate("LoginScreen")
          }}
        >
          <Text style={styles.linkText}>비밀번호 찾기</Text>
        </TouchableOpacity>

        <Text style={styles.linkText}>|</Text>
        <TouchableOpacity onPress={() => {
          navigation.navigate("SignUp1Screen")
        }}>
          <Text style={styles.linkText}>회원가입</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.divider}>
        <Text style={styles.dividerText}>SNS 계정으로 로그인</Text>
      </View>

      {/* 카카오, 네이버 로그인 버튼은 라이브러리나 직접 이미지로 구현해야 합니다. */}
      <View style={styles.oauth}>
        <TouchableOpacity>
          <Image
            style={styles.oauthImage}
            source={require("../../assets/images/kakao.png")}
          />
        </TouchableOpacity>
        <TouchableOpacity>
          <Image
            style={styles.oauthImage}
            source={require("../../assets/images/naver.png")}
          />
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 45,
    backgroundColor: "white",
  },
  image: {
    width: 200,
    height: 80,
    marginBottom: 60,
  },
  oauth: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    marginBottom: 20,
  },
  input: {
    width: "100%",
    height: 40,
    backgroundColor: "#D9D9D920",
    borderWidth: 1,
    borderColor: "#BAC0CA",
    borderRadius: 10,
    padding: 10,
    marginBottom: 10,
  },
  checkboxContainer: {
    flexDirection: "row",
    alignItems: "center",
    alignSelf: "flex-start", // 추가: 왼쪽 정렬
    marginBottom: 30,
  },
  checkbox: {
    width: 20,
    height: 20,
    borderRadius: 5,
    backgroundColor: "white",
    borderWidth: 1,
    marginRight: 10,
  },
  oauthImage: {
    width: 50,
    height: 50,
    marginLeft: 10,
    marginRight: 10,
  },
  button: {
    width: "100%",
    height: 40,
    backgroundColor: "#FF965C",
    borderRadius: 10,
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 10,
  },
  buttonText: {
    color: "#fff",
  },
  linksContainer: {
    flexDirection: "row",
    justifyContent: "space-between", // 수정: 양쪽 끝으로 확장
    width: "100%",
    marginBottom: 60,
    paddingHorizontal: 10,
  },
  divider: {
    width: "100%",
    height: 1,
    backgroundColor: "#BAC0CA",
    justifyContent: "center",
    alignItems: "center",
    position: "relative",
    marginBottom: 30, // 추가: 여백
  },
  dividerText: {
    position: "absolute", // 중앙에 배치하기 위해
    backgroundColor: "white", // 배경색으로 구분선 가리기
    paddingHorizontal: 10, // 좌우 패딩
    color: "#858585",
  },
  linkText: {
    color: "#858585",
  },
});
