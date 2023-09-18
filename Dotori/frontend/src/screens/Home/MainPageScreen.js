import React from "react";
import {
  View,
  Text,
  Image,
  TouchableOpacity,
  StyleSheet,
  FlatList,
  ScrollView,
} from "react-native";

import FooterScreen from "../Components/FooterScreen";

const banks = [
  {
    id: "1",
    name: "월급 통장",
    balance: "10,000원",
  },
  {
    id: "2",
    name: "비상금 통장",
    balance: "20,000원",
  },
  // {
  //   id: "3",
  //   name: "비상금 통장",
  //   balance: "20,000원",
  // },
  // {
  //   id: "4",
  //   name: "비상금 통장",
  //   balance: "20,000원",
  // },
  // {
  //   id: "5",
  //   name: "비상금 통장",
  //   balance: "20,000원",
  // },
  // ... 다른 은행들의 데이터
];

export default function MainPageScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <FlatList
        data={banks}
        ListHeaderComponent={
          <>
            <View style={styles.header}>
              <View></View>
              <Image
                style={styles.logo}
                source={require("../../assets/images/dotori_logo.png")}
              />
              <TouchableOpacity style={styles.helpButton}>
                <Text style={styles.helpIcon}>?</Text>
              </TouchableOpacity>
            </View>

            <Text style={styles.title}>당신의 소비를 계획해보세요!</Text>
            <Text style={styles.subtitle}>
              계좌를 등록하고 계획을 만들어 볼까요?
            </Text>

            <Image
              style={styles.rightImage}
              source={require("../../assets/images/Hamster/MainHamster.png")}
            />
          </>
        }
        renderItem={({ item }) => (
          <TouchableOpacity style={styles.bankContainer}>
            <Image
              style={styles.bankIcon}
              source={require("../../assets/images/logo_podo.png")}
            />
            <View>
              <Text style={styles.bankName}>{item.name}</Text>
              <Text style={styles.bankSubtitle}>잔액</Text>
            </View>
            <Text style={styles.bankBalance}>{item.balance}</Text>
          </TouchableOpacity>
        )}
        ListFooterComponent={
          <>
            <TouchableOpacity
              style={styles.addButton}
              onPress={() => navigation.navigate("OneCent1Screen")}
            >
              <Text style={styles.addText}>+</Text>
            </TouchableOpacity>
            <View style={{ marginTop: 50 }}></View>
          </>
        }
        keyExtractor={(item) => item.id}
      />
      <View style={styles.footer}>
        <FooterScreen navigation={navigation} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "white",
  },
  header: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 20,
  },
  logo: {
    width: 70,
    height: 70,
    resizeMode: "contain",
    marginLeft: 30,
  },
  helpButton: {
    width: 30,
    height: 30,
    borderRadius: 15,
    borderColor: "#BAC0CA",
    borderWidth: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  helpIcon: {
    fontSize: 16,
  },
  title: {
    fontSize: 25,
    // fontWeight: "bold",
    marginBottom: 5,
  },
  subtitle: {
    fontSize: 16,
    color: "#7B7B7B",
    marginBottom: 20,
  },
  rightImage: {
    alignSelf: "flex-end",
    width: 130,
    height: 130,
    marginBottom: 20,
  },
  bankContainer: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#FCAF17",
    borderRadius: 20,
    padding: 15,
    marginBottom: 10,
  },
  bankIcon: {
    width: 30,
    height: 30,
    resizeMode: "contain",
    marginRight: 10,
  },
  bankName: {
    fontSize: 18,
    marginBottom: 5,
  },
  bankSubtitle: {
    fontSize: 16,
  },
  bankBalance: {
    marginLeft: "auto",
    fontSize: 16,
  },
  addButton: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#FCAF17",
    borderRadius: 15,
    height: 50,
    borderStyle: "dashed", // 점선 테두리 추가
    marginVertical: 10, // 위아래로 여백 추가
  },
  addText: {
    color: "#FCAF17",
    fontSize: 24,
    fontWeight: "bold",
  },
  footer: {
    flex: 1,
    justifyContent: "flex-start",
    alignItems: "center",
    // marginTop: 220,
    marginBottom: -20,
  },
});
