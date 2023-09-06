import React, { useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
} from "react-native";
import { Ionicons } from "@expo/vector-icons";

import HeaderScreen from "../Header/HeaderScreen";

const { width } = Dimensions.get("window");

export default function TransferAmountScreen({ route, navigation }) {
  const [amount, setAmount] = useState("0원");
  const accountBalance = "10,000원"; // 이 값은 실제 계좌 잔액에 따라 바뀌어야 합니다.

  const { receiverBank, receiverAccount } = route.params;

  //   console.log(receiverAccount + "11receiverAccount");

  const appendAmount = (value) => {
    if (amount === "0원") {
      setAmount(`${value}원`);
    } else {
      setAmount(`${amount.slice(0, -1)}${value}원`);
    }
  };

  const removeLast = () => {
    if (amount.length > 2) {
      setAmount(`${amount.slice(0, -2)}`);
    } else {
      setAmount("0원");
    }
  };

  return (
    <View style={styles.container}>
      <HeaderScreen navigation={navigation} title="이체하기" />

      <Text style={styles.amountText}>{amount}</Text>
      <Text style={styles.balanceText}>출금 가능 금액: {accountBalance}</Text>

      <View style={styles.quickSelect}>
        {["100만", "10만", "5만", "1만", "전액"].map((quickAmount) => (
          <TouchableOpacity
            key={quickAmount}
            style={styles.quickSelectButton}
            onPress={() => setAmount(quickAmount)}
          >
            <Text>{quickAmount}</Text>
          </TouchableOpacity>
        ))}
      </View>

      <View style={styles.numPad}>
        {[
          ["1", "2", "3"],
          ["4", "5", "6"],
          ["7", "8", "9"],
          ["", "0", "<-"],
        ].map((row, rowIndex) => (
          <View key={rowIndex} style={styles.numRow}>
            {row.map((num) => (
              <TouchableOpacity
                key={num}
                style={styles.numButton}
                onPress={() => {
                  if (num === "<-") {
                    removeLast();
                  } else if (num !== "") {
                    appendAmount(num);
                  }
                }}
              >
                <Text style={styles.numText}>{num}</Text>
              </TouchableOpacity>
            ))}
          </View>
        ))}
      </View>

      <TouchableOpacity
        style={styles.confirmButton}
        onPress={() => {
          navigation.navigate("TransferDetailScreen", {
            amount: amount,
            receiverBank: receiverBank,
            receiverAccount: receiverAccount,
          });
        }}
      >
        <Text style={styles.confirmText}>확인</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },

  amountText: {
    fontSize: 36,
    textAlign: "center",
    marginVertical: 20,
  },
  balanceText: {
    fontSize: 16,
    textAlign: "center",
    marginBottom: 20,
  },
  quickSelect: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 20,
  },
  quickSelectButton: {
    padding: 10,
  },
  numPad: {
    flex: 1,
    justifyContent: "center",
  },
  numRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 20,
  },
  numButton: {
    width: width / 3 - 40,
    height: 60,
    justifyContent: "center",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#e0e0e0",
    borderRadius: 5,
  },
  numText: {
    fontSize: 24,
  },
  confirmButton: {
    backgroundColor: "purple",
    padding: 15,
    alignItems: "center",
    borderRadius: 5,
  },
  confirmText: {
    fontSize: 18,
    color: "white",
  },
});
