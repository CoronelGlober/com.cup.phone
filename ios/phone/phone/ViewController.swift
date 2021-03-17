//
//  ViewController.swift
//  phone
//
//  Created by Federico Lagarmilla on 3/16/21.
//

import UIKit
import core

class MessageCell: UITableViewCell {
    @IBOutlet weak var messageColorView: UIView!
    @IBOutlet weak var lblTime: UILabel!
    @IBOutlet weak var lblUser: UILabel!
    @IBOutlet weak var lblMessage: UILabel!
}

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, MessagesView {
    @IBOutlet weak var txtMessage: UITextField!
    @IBOutlet weak var tableView: UITableView!
    
    var messagesPresenter: MessagesPresenter?
    var messages: [Message] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setup()
    }

    func setup() {
        let locator = LocatorHelper()
        locator.setUp(databaseDriverFactory: DatabaseDriverFactory())
        messagesPresenter = MessagesPresenter(
            repository: locator.getRepository(),
            messagesClient: locator.getClient(),
            view: self
        )
        loadMessages()
    }

    func loadMessages() {
        guard let mPresenter = messagesPresenter else { return }
        mPresenter.startListeningForMessages()
    }
    
    @IBAction func sendMessage(sender: UIButton) {
        guard let mPresenter = messagesPresenter, let message = txtMessage.text else { return }
        mPresenter.sendMessage(message: message)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messages.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let message = messages[indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: "MessageCell", for: indexPath) as! MessageCell
        cell.lblMessage.text = message.message
        cell.lblUser.text = message.userName
        cell.messageColorView.backgroundColor = UIColor(colorCode: message.userColor.replacingOccurrences(of: "#", with: ""))
        cell.messageColorView.layer.cornerRadius = 5.0
        
        let coreFormatter = core.DateFormatter()
        cell.lblTime.text = coreFormatter.parseDate(timestamp: message.time)
        return cell
    }
    
    func clearMessageInput() {
        txtMessage.text = ""
    }
    
    func showMessages(message: [Message]) {
        messages = message
        messages.sort { $0.time < $1.time }
        tableView.reloadData()
        if messages.count > 0 {
            tableView.scrollToRow(
                at: IndexPath(row: messages.count - 1, section: 0),
                at: UITableView.ScrollPosition.bottom, animated: true
            )
        }
    }
}


extension UIColor {
    convenience init(colorCode: String, alpha: Float = 1.0) {
        let scanner = Scanner(string: colorCode)
        var rgbValue: UInt32 = 0
        scanner.scanHexInt32(&rgbValue)
        let mask: UInt32 = 0x000000FF
        let r = CGFloat((rgbValue >> 16) & mask) / 255.0
        let g = CGFloat((rgbValue >> 8) & mask) / 255.0
        let b = CGFloat(rgbValue & mask) / 255.0
        
        self.init(red: r, green: g, blue: b, alpha: CGFloat(alpha))
    }
    
    var hexString: String {
        var r: CGFloat = 0
        var g: CGFloat = 0
        var b: CGFloat = 0
        var a: CGFloat = 0
        getRed(&r, green: &g, blue: &b, alpha: &a)
        
        let red = (Int)(r * 255) << 16
        let green = (Int)(g * 255) << 8
        let blue = (Int)(b * 255) << 0
        let rgb: Int = red | green | blue
        return String(format: "#%06x", rgb)
    }
}
