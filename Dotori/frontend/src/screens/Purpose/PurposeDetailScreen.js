import React from "react";
import { View, StyleSheet, TouchableOpacity, Text, ScrollView } from "react-native";
import Feather from "react-native-vector-icons/Feather"; // Feather 아이콘을 가져올 수 있는 라이브러리
import { useDispatch, useSelector } from "react-redux";



const data = {
  id: "1",
  purposeTitle: "로마여행",
  currentBalance: 2000000,
  goalAmount: 10000000,
  purposeDataList: [
    {
      purposeDataSeq: 1,
      dataName: "시민주의 통장 1",
      dataAmount: 30000,
      dataCurrentBalance: 250000,
      dataCreateAt: "2023-09-10 13:25:00",
    },
    {
      purposeDataSeq: 2,
      dataName: "시민주의 통장 1",
      dataAmount: 10000,
      dataCurrentBalance: 240000,
      dataCreateAt: "2023-08-02 19:25:00",
    },
    {
      purposeDataSeq: 3,
      dataName: "시민주의 통장 1",
      dataAmount: 5000,
      dataCurrentBalance: 235000,
      dataCreateAt: "2023-08-02 18:10:00",
    },
    {
      purposeDataSeq: 4,
      dataName: "시민주의 통장 2",
      dataAmount: 5000,
      dataCurrentBalance: 230000,
      dataCreateAt: "2023-08-02 13:00:00",
    },
    {
      purposeDataSeq: 5,
      dataName: "시민주의 통장 1",
      dataAmount: 10000,
      dataCurrentBalance: 240000,
      dataCreateAt: "2023-08-01 19:25:00",
    }
  ]
}



export default function PurposeDetailScreen({ route, navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>{state.user.grantType})
  const accessToken =  useSelector((state)=>{state.user.accessToken})
  const refreshToken =  useSelector((state)=>{state.user.refreshToken})
  const dispatch = useDispatch()
  // 그 외

  const { itemId } = route.params;

  // 날짜 형식을 변환하는 함수
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${month}월 ${day}일`;
  };

  const formatTime = (dateString) => {
    const date = new Date(dateString);
    const hours = date.getHours();
    const minutes = date.getMinutes();
    return `${hours}:${minutes}`;
  }

  // 날짜를 그룹화할 객체 생성
  const dateGroups = {};

  // 날짜별로 항목을 그룹화
  data.purposeDataList.forEach((item) => {
    const dateKey = formatDate(item.dataCreateAt);
    if (!dateGroups[dateKey]) {
      dateGroups[dateKey] = [];
    }
    dateGroups[dateKey].push(item);
  });

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <View style={{ flex: 1 }} />
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Feather name="x" size={30} style={styles.closeIcon} />
        </TouchableOpacity>
      </View>

      {/* Title Container */}
      <View style={styles.titleContainer}>
        <Text style={styles.purposeTitle}>{data.purposeTitle}</Text>
        <Text style={styles.titleRight}>{data.currentBalance.toLocaleString()}원</Text>
      <Text style={styles.goalAmountText}>목표 금액</Text>
      <Text style={styles.goalAmount}>{data.goalAmount.toLocaleString()}원</Text>
      </View>

      {/* 구분선 */}
      <View style={styles.divider} />

      {/* Purpose Data 날짜별로 묶기 */}
      <ScrollView 
        style={styles.scrollContainer}
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}
      >
        {Object.entries(dateGroups).map(([date, items]) => (
          <View style={styles.dateGroup} key={date}>
            <Text style={styles.dateText}>{date}</Text>
            {items.map((item) => (
              <View style={styles.dataBox} key={item.purposeDataSeq}>
                <View style={styles.dataBoxInTop}key={item.purposeDataSeq}>
                  <Text style={styles.timeText}>{formatTime(item.dataCreateAt)}</Text>
                  <Text style={styles.dataNameText}>{item.dataName}</Text>
                  <Text style={styles.dataAmountText}>{item.dataAmount.toLocaleString()}원</Text>
                </View>
                <View style={styles.dataBoxInBottom}>
                  <Text style={styles.dataCurrentBalanceText}>{item.dataCurrentBalance.toLocaleString()}원</Text>
                </View>
              </View>
            ))}
          </View>
        ))}

        <View style={styles.purposeStopContainer}>
          <TouchableOpacity
            style={styles.stopPurposeButton}
            onPress={() => navigation.navigate("PurposeStopScreen")}
          >
            <Text style={styles.stopPurposeText}>목표 중단하기</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "white",
    padding: 16,
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    width: "100%",
    justifyContent: "flex-end",
    paddingVertical: 10,
    marginTop: 20,
    marginBottom: 20,
  },
  titleContainer: {
    width: "90%",
    alignItems: "left",
    marginBottom: 20,
  },
  purposeTitle: {
    fontSize: 30,
    fontWeight: "bold",
    marginBottom: 20,
  },
  titleRight: {
    fontSize: 30,
    fontWeight: "bold",
    color: "#FF965C",
    alignSelf: "flex-end",
  },
  goalAmountText: {
    fontSize: 15,
    marginBottom: 5,
  },
  goalAmount: {
    fontSize: 15,
    fontWeight: "bold",
  },
  divider: {
    width: "100%",
    height: 2,
    backgroundColor: "#D0D4DA",
    marginBottom: 10,
  },
  scrollContainer: {
    marginTop: 20,
    width: "100%",
  },
  scrollContent: {
    flexGrow: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  dateContainer: {
    flexDirection: "column",
    alignItems: "flex-start",
    marginBottom: 20,
  },
  dateText: {
    fontSize: 15,
    fontWeight: "bold",
    marginBottom: 10,
    marginTop: 20,
  },
  dataBox: {
    width: "100%",
    height: 80,
    justifyContent: "space-between",
    borderWidth: 1,
    borderColor: "#D0D4DA",
    borderRadius: 10,
    padding: 15,
    marginBottom: 10,
  },
  dataBoxInTop: {
    width: "90%",
    flexDirection: "row",
    justifyContent: "space-between",
  },
  timeText: {
    alignSelf: "center",
    marginRight: 13,
  },
  dataNameText: {
    fontSize: 15,
    flex: 1,
    alignSelf: "center",
  },
  dataAmountText: {
    fontSize: 15,
    alignSelf: "center",
  },
  dataBoxInBottom: {
    fontSize: 15,
    width: "90%",
    flexDirection: "row",
    alignSelf: "flex-end",
    marginBottom: 10,
  },
  dataCurrentBalanceText: {
    fontSize: 13,
    alignSelf: "flex-end",
    color: "#858585",
  },
  closeIcon: {
    marginRight: 20,
  },
  purposeStopContainer: {
    width: "90%",
    alignItems: "flex-end",
    justifyContent: "center",
    margin: 20,
  },
  stopPurposeButton: {
    alignItems: "center",
    justifyContent: "center",
    marginRight: 10,
  },
  stopPurposeText: {
    fontSize: 13,
    color: "#939393",
  },
});
