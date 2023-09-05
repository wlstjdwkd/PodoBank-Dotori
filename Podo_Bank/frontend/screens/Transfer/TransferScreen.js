import React, { useState, useRef } from "react";
import {
  View,
  Text,
  StyleSheet,
  Button,
  TouchableOpacity,
  TextInput,
  Modal,
  Animated,
  Dimensions,
} from "react-native";
import { LinearGradient } from "expo-linear-gradient";
import { Ionicons } from "@expo/vector-icons";

const { height, width } = Dimensions.get("window");

// 더미 데이터 (실제 데이터와 연동 필요)
const recentAccounts = [
  { name: "홍길동", accountNumber: "1234-5678-9101", description: "SSAFY" },
  { name: "방진성", accountNumber: "1234-5678-3101", description: "SSAFY" },
  // ... 필요하면 더 추가
];

export default function TransferScreen({ navigation }) {
  const [modalVisible, setModalVisible] = useState(false);
  const translateY = useRef(new Animated.Value(height)).current;
  const [accountInput, setAccountInput] = useState("");

  const [receiverBank, setReceiverBank] = useState("PODO BANK");

  const handleKeyPress = (key) => {
    if (key === "<-") {
      setAccountInput((prev) => prev.slice(0, -1));
    } else {
      setAccountInput((prev) => prev + key);
    }
  };

  const openModal = () => {
    setModalVisible(true);
    Animated.timing(translateY, {
      toValue: height * 0.4,
      duration: 500,
      useNativeDriver: true,
    }).start();
  };

  const closeModal = () => {
    Animated.timing(translateY, {
      toValue: height,
      duration: 500,
      useNativeDriver: true,
    }).start(() => {
      setModalVisible(false);
    });
  };

  return (
    <LinearGradient colors={["#6BB5FF", "white"]} style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={styles.backButton}>&lt;</Text>
        </TouchableOpacity>
        <Text style={styles.headerTitle}>이체하기</Text>
        <TouchableOpacity onPress={() => navigation.navigate("HomeScreen")}>
          <Ionicons name="home" size={24} color="black" />
        </TouchableOpacity>
      </View>

      <Text style={styles.receiverLabel}>받는분</Text>

      <TextInput placeholder="PODO BANK" style={styles.bankLabel}></TextInput>
      <View style={styles.line} />
      <TextInput
        style={styles.input}
        placeholder="계좌번호"
        textAlign="center" // 중앙 정렬
        onFocus={openModal}
        blurOnSubmit={true}
        onSubmitEditing={closeModal}
      />
      <View style={styles.line} />
      <Text style={styles.centerLabel}>최근 보낸 계좌</Text>
      <View style={styles.line} />
      {recentAccounts.map((account, index) => (
        <View key={index}>
          <View style={styles.accountItem}>
            <Text style={styles.accountName}>{account.name}</Text>
          </View>
          <View style={styles.accountItem}>
            <Text>
              <Text style={styles.accountDetail}>{account.description}</Text>
              <Text style={styles.accountNumber}>{account.accountNumber}</Text>
            </Text>
          </View>
          <View style={styles.line} />
        </View>
      ))}
      {modalVisible && (
        <Modal
          transparent={true}
          animationType="slide"
          visible={modalVisible}
          onRequestClose={closeModal}
        >
          <Animated.View
            style={{
              transform: [{ translateY: translateY }],
              height: "60%",
              width: "100%", // 가로로 꽉 차게 수정
              backgroundColor: "white",
              borderTopLeftRadius: 20,
              borderTopRightRadius: 20,
            }}
          >
            <View
              style={{
                flexDirection: "row",
                justifyContent: "space-between",
                alignItems: "center",
                paddingHorizontal: 10,
                paddingVertical: 5,
              }}
            >
              <Text>계좌번호</Text>
              <TouchableOpacity onPress={closeModal}>
                <Text>X</Text>
              </TouchableOpacity>
            </View>

            <TextInput
              style={{ ...styles.input, marginHorizontal: 10 }}
              value={accountInput}
              editable={false}
              placeholder="계좌번호 입력"
              onChangeText={(text) => setAccountInput(text)}
            />

            <View style={{ alignItems: "center", marginVertical: 10 }}>
              {[
                ["1", "2", "3"],
                ["4", "5", "6"],
                ["7", "8", "9"],
                ["", "0", "<-"],
              ].map((row, rowIndex) => (
                <View
                  key={rowIndex}
                  style={{ flexDirection: "row", marginBottom: 10 }}
                >
                  {row.map((key) => (
                    <TouchableOpacity
                      key={key}
                      style={{
                        width: width / 3,
                        height: 60,
                        justifyContent: "center",
                        alignItems: "center",
                        borderWidth: 1,
                        borderColor: "grey",
                      }}
                      onPress={() => handleKeyPress(key)}
                    >
                      <Text>{key}</Text>
                    </TouchableOpacity>
                  ))}
                </View>
              ))}
            </View>

            <TouchableOpacity
              style={{
                backgroundColor: "purple",
                margin: 20,
                padding: 15,
                borderRadius: 5,
                alignItems: "center",
              }}
              onPress={() =>
                navigation.navigate("TransferAmountScreen", {
                  receiverBank: receiverBank,
                  receiverAccount: accountInput,
                })
              }
            >
              <Text style={{ color: "white" }}>확인</Text>
            </TouchableOpacity>
          </Animated.View>
        </Modal>
      )}
    </LinearGradient>
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
  receiverLabel: {
    fontSize: 16,
    marginTop: 100,
    marginBottom: 10,
  },
  backButton: {
    fontSize: 24,
    fontWeight: "bold",
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: "bold",
  },
  bankLabel: {
    fontSize: 16,
    textAlign: "center", // 중앙 정렬
    marginTop: 80, // 위치 조정이 필요할 수 있습니다.
    marginBottom: 10,
  },
  input: {
    height: 40,
    borderColor: "transparent",
    borderBottomColor: "grey",
    borderWidth: 1,
    marginBottom: 20,
  },
  line: {
    height: 1,
    backgroundColor: "grey",
    marginBottom: 20,
  },
  centerLabel: {
    fontSize: 16,
    textAlign: "center",
    marginTop: 100,
    marginBottom: 20,
  },

  accountItem: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 5,
  },
  accountName: {
    fontSize: 18,
    marginBottom: 5,
  },
  accountDetail: {
    fontSize: 16,
    marginRight: 10,
  },
  accountNumber: {
    fontSize: 16,
  },
});
