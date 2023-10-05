import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  Image,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
} from "react-native";

export default function RandomBox3Screen({ navigation, route }) {
  // const coin = route.params.coin;
  const [prizeAmount, setPrizeAmount] = useState(route.params.prizeAmount)

  const windowWidth = Dimensions.get('window').width;
  const windowHeight = Dimensions.get('window').height;


  useEffect(()=>{
    
  }, [])
  return (
    <View style={{flex:1, backgroundColor:'white'}}>
      <View style={{flex:0.15}}></View>

      <View style={styles.container}>
        {/* <View style={{}}>
          <Text style={{fontWeight:"bold", fontSize:26}}>RANDOM BOX</Text>
        </View> */}
        <View
          onPress={()=>{
            // navigation.navigate("RandomBox3Screen", {prizeAmount:prizeAmount})
          }}
        >
          <Image
            source={require("../../assets/images/Hamster/giftHamster2.png")}
            style={{width:windowWidth*0.7, height:windowWidth*0.7}}
          />
        </View>
        <View>
          <Text style={{fontSize:40, fontWeight:"bold"}}>{prizeAmount}원</Text>
          <Text style={{textAlign:'center'}}>축하합니다!</Text>
        </View>
      </View>
      <View style={{width: "80%", alignSelf: "center", alignItems:'center',}}>
        {/* 버튼 */}
        <TouchableOpacity
            style={[
              styles.button,
            ]}
            onPress={() => {
              navigation.navigate("RandomBox4Screen",{prizeAmount:prizeAmount})
            }}
          >
            <Text style={styles.buttonText}>저축하기</Text>
          </TouchableOpacity>
      </View>
        
        <View style={{flex:0.2}}></View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 0.6,
    alignSelf: "center",
    alignItems:'center',
    // justifyContent: "center",
    justifyContent: "space-evenly",
    width: "80%"
  },
  questionMark:{
    position:'absolute',
    // right: 50,
    // top: 100
  },
  button: {
    backgroundColor: "#FF965C",
    borderRadius: 8,
    width: "100%",
    padding: 10,
    alignItems: "center",
    // marginTop: 35,
    marginTop: 15,
  },
  buttonText: {
    fontSize: 15,
    color: "white",
    fontWeight: "bold",
  },
});